package com.certificator.patron_ms.certificate;

import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.shared.dto.ApiResponse;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CertificateControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    @Autowired
    private ObjectMapper objectMapper;

    private String readJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    private TestRestTemplate asAdmin() {
        return restTemplate.withBasicAuth("admin", "admin123");
    }

    private TestRestTemplate asUser() {
        return restTemplate.withBasicAuth("user", "user123");
    }

    private Long insertedCertId;

    @BeforeEach
    void setup() throws IOException {
        String json = readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        // Enviar el certificado antes de cada test
        ResponseEntity<String> response = asAdmin().postForEntity("/api/patrones", request, String.class);

        ApiResponse<Certificate> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<ApiResponse<Certificate>>() {});
        insertedCertId = responseObj.getData().getId(); 
    }

    @Test
    void createCertificate_asAdmin_shouldReturnCreated() throws Exception {
        String json = readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = asAdmin().postForEntity("/api/patrones", request, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<Certificate> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<ApiResponse<Certificate>>() {});
        Certificate cert = objectMapper.convertValue(responseObj.getData(), Certificate.class);

        assertNotNull(cert);
        assertEquals("CERT-001", cert.getCertificateNumber());
    }

    @Test
    void createCertificate_asUser_shouldReturnCreated() throws Exception {
        String json = readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<String> response = asUser().postForEntity("/api/patrones", request, String.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }

    @Test
    void getAllCertificates_asUser_shouldReturnList() throws Exception {

        ResponseEntity<String> response = asUser().getForEntity("/api/patrones", String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<List<Certificate>> apiResponse = objectMapper.readValue(
            response.getBody(),
            new TypeReference<ApiResponse<List<Certificate>>>() {}
        );
        List<Certificate> certList = apiResponse.getData();

        assertNotNull(certList);
        assertTrue(certList.size() >= 3);
}

    @Test
    void getCertificateById_asUser_shouldReturnCertificate() {
        ResponseEntity<Certificate> response = asUser().getForEntity("/api/patrones/" + insertedCertId, Certificate.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteCertificate_asAdmin_shouldRemoveIt() {
        asAdmin().delete("/api/patrones/1");

        ResponseEntity<String> response = asAdmin().getForEntity("/api/patrones/1", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    void deleteCertificate_asUser_shouldRemoveIt() {
        ResponseEntity<Void> response = asUser().exchange("/api/patrones/" + insertedCertId, HttpMethod.DELETE, null, Void.class);
        assertEquals(HttpStatus.FORBIDDEN, response.getStatusCode());
    }
}
