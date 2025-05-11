package com.certificator.patron_ms.certificate;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.certificator.patron_ms.certificate.CertificateRepository;

@SpringBootTest
public class CertificateRepositoryTest {


    @Autowired
    private CertificateRepository certificateRepository;

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
