package com.certificator.patron_ms.CertificateTests;

import com.certificator.patron_ms.Model.Certificate;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.*;
import org.springframework.util.StreamUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class CertificateControllerIntegrationTest {

    @Autowired
    private TestRestTemplate restTemplate;

    private String readJson(String path) throws IOException {
        ClassPathResource resource = new ClassPathResource(path);
        return StreamUtils.copyToString(resource.getInputStream(), StandardCharsets.UTF_8);
    }

    @Test
    void createCertificate_shouldReturnCreated() throws Exception {
        String json = readJson("test-data/certificate-with-measurements.json");

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        HttpEntity<String> request = new HttpEntity<>(json, headers);

        ResponseEntity<Certificate> response = restTemplate.postForEntity("/api/patrones", request, Certificate.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
        assertEquals("CERT-001", response.getBody().getCertificateNumber());
    }

    @Test
    void getAllCertificates_shouldReturnList() throws Exception {
        String json = readJson("test-data/certificate-with-measurementsVarios.json");

        Certificate[] certificates = restTemplate.getForObject("/api/patrones", Certificate[].class);
        assertNotNull(certificates);
        assertTrue(certificates.length >= 3);
    }

    @Test
    void getCertificateById_shouldReturnCertificate() {
        ResponseEntity<Certificate> response = restTemplate.getForEntity("/api/patrones/1", Certificate.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertNotNull(response.getBody());
    }

    @Test
    void deleteCertificate_shouldRemoveIt() {
        restTemplate.delete("/api/patrones/1");

        ResponseEntity<String> response = restTemplate.getForEntity("/api/patrones/1", String.class);
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode()); // O cambia si devuelves 404
    }
}
