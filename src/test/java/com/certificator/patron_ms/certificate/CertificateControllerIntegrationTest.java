package com.certificator.patron_ms.certificate;

import com.certificator.patron_ms.shared.dto.ApiResponse;
import com.certificator.patron_ms.user.User;
import com.certificator.patron_ms.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CertificateControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    @Autowired
    private CertificateRepository certificateRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    private Long insertedCertId;
    private String adminToken;
    private String userToken;

    @AfterEach
    void deleteUsers(){
        userRepository.findByUsername("adminTest").ifPresent(userRepository::delete);
        userRepository.findByUsername("userTest").ifPresent(userRepository::delete);
    }

    @BeforeAll
    void setup() throws IOException {

        String adminToken = registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");
        String json = readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.exchange(
            "/api/patrones", HttpMethod.POST, request, String.class
        );

        assertEquals(HttpStatus.OK, response.getStatusCode(), "Error al insertar el certificado en setup(): " + response.getBody());

        ApiResponse<Certificate> responseObj =
            objectMapper.readValue(response.getBody(), new TypeReference<ApiResponse<Certificate>>() {});
        insertedCertId = responseObj.getData().getId();
    }

    @AfterAll
    void cleanupAll() {
        certificateRepository.deleteAll();   
    }

    private String readJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    void createCertificate_asUser_shouldReturnForbidden() throws Exception {

        String userToken = registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        String json = readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(userToken);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones", request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        deleteTestUser("userTest");
    }


    @Test
    void getAllCertificates_asUser_shouldReturnList() throws Exception {

        String userToken = registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/patrones", HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        deleteTestUser("userTest");

    }

    @Test
    void getCertificateById_asUser_shouldReturnCertificate() {

        String userToken = registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<Certificate> response = restTemplate.exchange("/api/patrones/" + insertedCertId, HttpMethod.GET, request, Certificate.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());

        deleteTestUser("userTest");
    }

    @Test
    void createAndDeleteCertificate_asAdmin_shouldSucceed() throws Exception {
        // Crear usuario admin
        String adminToken = registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        // Crear certificado
        String json = readJson("test-data/certificate-with-measurements.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/patrones", request, String.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ApiResponse<Certificate> responseObj =
            objectMapper.readValue(createResponse.getBody(), new TypeReference<>() {});
        Long certId = responseObj.getData().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            "/api/patrones/" + certId, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class
        );
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        deleteTestUser("adminTest");
    }


    @Test
    void deleteCertificate_asUser_shouldBeForbidden() throws Exception {
        // Crear admin y obtener token
        String adminToken = registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        // Crear certificado con admin
        String json = readJson("test-data/certificate-with-measurements.json");
        HttpHeaders adminHeaders = new HttpHeaders();
        adminHeaders.setContentType(MediaType.APPLICATION_JSON);
        adminHeaders.setBearerAuth(adminToken);
        HttpEntity<String> adminRequest = new HttpEntity<>(json, adminHeaders);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/patrones", adminRequest, String.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ApiResponse<Certificate> responseObj =
            objectMapper.readValue(createResponse.getBody(), new TypeReference<>() {});
        Long certId = responseObj.getData().getId();

        // Crear usuario normal
        String userToken = registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        // Intentar eliminar con usuario
        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            "/api/patrones/" + certId, HttpMethod.DELETE, userRequest, Void.class
        );

        // Debe estar prohibido
        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());

        // Limpiar usuarios
        deleteTestUser("adminTest");
        deleteTestUser("userTest");
    }


    private void createTestUser(String username, String password, String email, String role) {
        if (userRepository.existsByUsername(username)) {
            userRepository.delete(userRepository.findByUsername(username).get());
        }

        User user = new User();
        user.setUsername(username);
        user.setPassword(passwordEncoder.encode(password)); // Solo aquí se codifica
        user.setEmail(email);
        user.setRole(role);

        userRepository.save(user);
    }

    private void deleteTestUser(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }

    private String registerAndLogin(String username, String password, String email, String role) {
        createTestUser(username, password, email, role);
        return getToken(username, password);
    }

    private String getToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        String body = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/auth/login", request, Map.class);
        if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null || !response.getBody().containsKey("token")) {
            throw new RuntimeException("No se pudo obtener el token. Login fallido.\nStatus: "
                + response.getStatusCode() + "\nBody: " + response.getBody());
        }

        return (String) response.getBody().get("token");
    }
}
