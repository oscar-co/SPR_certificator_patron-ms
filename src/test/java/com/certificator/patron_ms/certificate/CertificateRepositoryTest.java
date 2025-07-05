package com.certificator.patron_ms.certificate;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.test.context.ActiveProfiles;

import com.certificator.patron_ms.user.UserRepository;
import com.certificator.patron_ms.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;


// Usamos @TestInstance(TestInstance.Lifecycle.PER_CLASS) para permitir que los métodos @AfterAll y @BeforeAll
// sean no estáticos (non-static). Esto es útil cuando necesitamos acceder a propiedades de instancia,
// como beans inyectados con @Autowired, dentro de esos métodos.
// Por defecto, JUnit 5 requiere que @AfterAll/@BeforeAll sean static, pero este ajuste cambia el ciclo de vida
// para que JUnit reutilice la misma instancia de clase en todos los tests.
@ActiveProfiles("test")
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
public class CertificateRepositoryTest {

    @Autowired private UserRepository userRepository;
    @Autowired private TestUtils testUtils;
    @Autowired private CertificateRepository certificateRepository;


    private String userToken;

    @BeforeAll
    void isert() throws IOException{
        testUtils.insertListOfCertificates();
    }

    @BeforeEach
    void setup() {
        userToken = testUtils.registerAndLogin("userConv", "user123", "user@conv.com", "ROLE_USER");
    }

    @AfterEach
    void cleanup() {
        userRepository.findByUsername("userConv").ifPresent(userRepository::delete);
    }

    @AfterAll
    void cleanupAll() {
        certificateRepository.deleteAll();   
    }

    

    @Test
    void testFindUncertaintyAboveReferenceByNameIdentify_1() {

        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify("PTN-002", 42.0);
        assertTrue(uncertainty.isPresent());
        assertEquals(0.12, uncertainty.get());
    }

    @Test
    void testFindUncertaintyAboveReferenceByNameIdentify_2() {

        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify("PTN-001", 5.0);
        assertTrue(uncertainty.isPresent());
        assertEquals(0.15, uncertainty.get());
    }

    @Test
    void testFindUncertaintyAboveReferenceByNameIdentify_3() {

        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify("PTN-001", -0.9);
        assertTrue(uncertainty.isPresent());
        assertEquals(0.05, uncertainty.get());
    }

    @Test
    void testFindUncertainty_OutOfRange_Up() {

        Optional<Double> uncertainty = certificateRepository.findUncertaintyAboveReferenceByNameIdentify("PTN-001", 100.9);
        assertTrue(uncertainty.isEmpty());
    }
}
