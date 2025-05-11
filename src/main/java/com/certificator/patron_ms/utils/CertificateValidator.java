package com.certificator.patron_ms.utils;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.Certificate.Certificate;

@Component
public class CertificateValidator {

    public void validate(Certificate certificate) {
        if (certificate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Certificate cannot be null");
        }
        if (certificate.getNameIdentify() == null || certificate.getNameIdentify().isBlank()) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Certificate name is required");
        }
    }
}