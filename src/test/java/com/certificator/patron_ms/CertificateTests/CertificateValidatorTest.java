package com.certificator.patron_ms.certificateTests;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.shared.utils.CertificateValidator;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.web.server.ResponseStatusException;

public class CertificateValidatorTest {

    private CertificateValidator validator;

    @BeforeEach
    void setUp() {
        validator = new CertificateValidator();
    }

    @Test
    void validate_whenCertificateIsNull_throwsBadRequest() {
        assertThrows(ResponseStatusException.class, () -> {
            validator.validate(null);
        });
    }

    @Test
    void validate_whenNameIdentifyIsNull_throwsBadRequest() {
        Certificate cert = new Certificate();
        cert.setNameIdentify(null);

        assertThrows(ResponseStatusException.class, () -> {
            validator.validate(cert);
        });
    }

    @Test
    void validate_whenNameIdentifyIsBlank_throwsBadRequest() {
        Certificate cert = new Certificate();
        cert.setNameIdentify("   ");  // blank string

        assertThrows(ResponseStatusException.class, () -> {
            validator.validate(cert);
        });
    }

    @Test
    void validate_whenValidCertificate_doesNotThrow() {
        Certificate cert = new Certificate();
        cert.setNameIdentify("PTN-001");

        assertDoesNotThrow(() -> {
            validator.validate(cert);
        });
    }
}