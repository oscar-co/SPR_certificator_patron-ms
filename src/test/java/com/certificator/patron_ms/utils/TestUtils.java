package com.certificator.patron_ms.utils;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;
import java.util.Map;

import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.shared.dto.ApiResponse;
import com.certificator.patron_ms.user.User;
import com.certificator.patron_ms.user.UserRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.util.StreamUtils;

@Component
public class TestUtils {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private UserRepository userRepository;
    @Autowired private PasswordEncoder passwordEncoder;
    @Autowired private ObjectMapper objectMapper;

    public static String readJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    public void insertListOfCertificates() throws IOException {
        String adminToken = registerAndLogin("adminTest", "admin123", "admin@test.com", "ROLE_ADMIN");

        // Leer el JSON como una lista de certificados
        String json = readJson("test-data/certificate-with-measurementsVarios.json");
        System.out.println(json);
        ObjectMapper mapper = new ObjectMapper();
        mapper.registerModule(new JavaTimeModule());  // Para manejar LocalDate
        mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false); 
        List<Certificate> certs = mapper.readValue(json, new TypeReference<List<Certificate>>() {});

        for (Certificate cert : certs) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setBearerAuth(adminToken);

            HttpEntity<String> request = new HttpEntity<>(mapper.writeValueAsString(cert), headers);
            ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones", request, String.class);

            assertEquals(HttpStatus.OK, response.getStatusCode(), "Error insertando certificado: " + cert.getNameIdentify());
        }

        // Verificar que hay al menos 3 certificados insertados
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(adminToken);
        HttpEntity<Void> getRequest = new HttpEntity<>(headers);

        ResponseEntity<String> getResponse = restTemplate.exchange("/api/patrones", HttpMethod.GET, getRequest, String.class);
        assertEquals(HttpStatus.OK, getResponse.getStatusCode());

        ApiResponse<List<Certificate>> responseObj = mapper.readValue(
            getResponse.getBody(),
            new TypeReference<ApiResponse<List<Certificate>>>() {}
        );

        assertTrue(responseObj.getData().size() == 3, "Se esperaban al menos 3 certificados");
    }


    public String registerAndLogin(String username, String password, String email, String role) {
        createTestUser(username, password, email, role);
        return getToken(username, password);
    }

    private void createTestUser(String username, String password, String email, String role) {
        userRepository.findByUsername(username).ifPresent(userRepository::delete);
        User user = new User(null, username, passwordEncoder.encode(password), email, role);
        userRepository.save(user);
    }

    private String getToken(String username, String password) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        String body = String.format("{\"username\":\"%s\", \"password\":\"%s\"}", username, password);
        HttpEntity<String> request = new HttpEntity<>(body, headers);

        ResponseEntity<Map> response = restTemplate.postForEntity("/api/auth/login", request, Map.class);

        if (response.getStatusCode() != HttpStatus.OK || !response.getBody().containsKey("token")) {
            throw new RuntimeException("Login fallido");
        }

        return (String) response.getBody().get("token");
    }

    public static String readJsonFromFile(String relativePath) throws Exception {
        ClassLoader classLoader = TestUtils.class.getClassLoader();
        URL resource = classLoader.getResource(relativePath);
        if (resource == null) {
            throw new IllegalArgumentException("File not found: " + relativePath);
        }
        return new String(Files.readAllBytes(Paths.get(resource.toURI())));
    }

    public HttpHeaders buildHeaders(String token) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setBearerAuth(token);
        return headers;
    }
}
