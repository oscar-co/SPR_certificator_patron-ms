package com.certificator.patron_ms.certificate;

import com.certificator.patron_ms.shared.dto.ApiResponse;
import com.certificator.patron_ms.user.UserRepository;
import com.certificator.patron_ms.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@ActiveProfiles("test")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CertificateControllerIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private CertificateRepository certificateRepository;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtils testUtils;

    private Long insertedCertId;

    @AfterEach
    void deleteUsers(){
        userRepository.findByUsername("adminTest").ifPresent(userRepository::delete);
        userRepository.findByUsername("userTest").ifPresent(userRepository::delete);
    }

    @BeforeAll
    void setup() throws IOException {

        String adminToken = testUtils.registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");
        String json = TestUtils.readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = testUtils.buildHeaders(adminToken);
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

    @Test
    void createCertificate_asUser_shouldReturnForbidden() throws Exception {

        String userToken = testUtils.registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        String json = TestUtils.readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = testUtils.buildHeaders(userToken);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones", request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());

        deleteTestUser("userTest");
    }


    @Test
    void getAllCertificates_asUser_shouldReturnList() throws Exception {

        String userToken = testUtils.registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate.exchange("/api/patrones", HttpMethod.GET, request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        deleteTestUser("userTest");

    }

    @Test
    void getCertificateById_asUser_shouldReturnCertificate() {

        String userToken = testUtils.registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

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
        String adminToken = testUtils.registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        // Crear certificado
        String json = TestUtils.readJson("test-data/certificate-with-measurements.json");
        HttpHeaders headers = testUtils.buildHeaders(adminToken);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/patrones", request, String.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ApiResponse<Certificate> responseObj =
            objectMapper.readValue(createResponse.getBody(), new TypeReference<>() {});
        Long certificate_id = responseObj.getData().getId();

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            "/api/patrones/" + certificate_id, HttpMethod.DELETE, new HttpEntity<>(headers), Void.class
        );
        assertEquals(HttpStatus.OK, deleteResponse.getStatusCode());

        deleteTestUser("adminTest");
    }


    @Test
    void deleteCertificate_asUser_shouldBeForbidden() throws Exception {
        // Crear admin y obtener token
        String adminToken = testUtils.registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        String json = TestUtils.readJson("test-data/certificate-with-measurements.json");
        HttpHeaders adminHeaders = testUtils.buildHeaders(adminToken);
        HttpEntity<String> adminRequest = new HttpEntity<>(json, adminHeaders);

        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/patrones", adminRequest, String.class);
        assertEquals(HttpStatus.OK, createResponse.getStatusCode());

        ApiResponse<Certificate> responseObj =
            objectMapper.readValue(createResponse.getBody(), new TypeReference<>() {});
        Long certificate_id = responseObj.getData().getId();

        String userToken = testUtils.registerAndLogin("userTest", "user123", "user@test.com", "ROLE_USER");

        HttpHeaders userHeaders = new HttpHeaders();
        userHeaders.setBearerAuth(userToken);
        HttpEntity<Void> userRequest = new HttpEntity<>(userHeaders);

        ResponseEntity<Void> deleteResponse = restTemplate.exchange(
            "/api/patrones/" + certificate_id, HttpMethod.DELETE, userRequest, Void.class
        );

        // Debe estar prohibido
        assertEquals(HttpStatus.FORBIDDEN, deleteResponse.getStatusCode());

        // Limpiar usuarios
        deleteTestUser("adminTest");
        deleteTestUser("userTest");
    }


    // private void createTestUser(String username, String password, String email, String role) {
    //     if (userRepository.existsByUsername(username)) {
    //         userRepository.delete(userRepository.findByUsername(username).get());
    //     }

    //     User user = new User();
    //     user.setUsername(username);
    //     user.setPassword(passwordEncoder.encode(password));
    //     user.setEmail(email);
    //     user.setRole(role);

    //     userRepository.save(user);
    // }

    private void deleteTestUser(String username) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
    }

    // private String registerAndLogin(String username, String password, String email, String role) {
    //     createTestUser(username, password, email, role);
    //     return getToken(username, password);
    // }

    // private String getToken(String username, String password) {
    //     HttpHeaders headers = new HttpHeaders();
    //     headers.setContentType(MediaType.APPLICATION_JSON);

    //     String body = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
    //     HttpEntity<String> request = new HttpEntity<>(body, headers);

    //     ResponseEntity<Map> response = restTemplate.postForEntity("/api/auth/login", request, Map.class);
    //     if (response.getStatusCode() != HttpStatus.OK || response.getBody() == null || !response.getBody().containsKey("token")) {
    //         throw new RuntimeException("No se pudo obtener el token. Login fallido.\nStatus: "
    //             + response.getStatusCode() + "\nBody: " + response.getBody());
    //     }

    //     return (String) response.getBody().get("token");
    // }

    @Test
    void updateCertificate_shouldUpdateCorrectly() throws Exception {
        String adminToken = testUtils.registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        // Crear certificado
        String json = TestUtils.readJson("test-data/certificate-with-measurements.json");
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);
        HttpEntity<String> request = new HttpEntity<>(json, headers);
        ResponseEntity<String> createResponse = restTemplate.postForEntity("/api/patrones", request, String.class);
        Long certificate_id = objectMapper.readValue(createResponse.getBody(), new TypeReference<ApiResponse<Certificate>>() {}).getData().getId();

        // Modificar nombre
        Certificate updatedCert = objectMapper.readValue(json, Certificate.class);
        updatedCert.setNameIdentify("UPDATED-PTN");
        String updatedJson = objectMapper.writeValueAsString(updatedCert);
        HttpEntity<String> updateRequest = new HttpEntity<>(updatedJson, headers);

        ResponseEntity<String> updateResponse = restTemplate.exchange(
            "/api/patrones/" + certificate_id, HttpMethod.PUT, updateRequest, String.class);

        assertEquals(HttpStatus.OK, updateResponse.getStatusCode());
        assertTrue(updateResponse.getBody().contains("UPDATED-PTN"));

        deleteTestUser("adminTest");
    }

    @Test
    void createCertificate_withInvalidData_shouldReturnBadRequest() {
        String adminToken = testUtils.registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        String invalidJson = "{}"; // vacío o incorrecto
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(adminToken);

        HttpEntity<String> request = new HttpEntity<>(invalidJson, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones", request, String.class);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        deleteTestUser("adminTest");
    }


}
