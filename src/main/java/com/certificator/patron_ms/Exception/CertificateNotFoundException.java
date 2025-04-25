package com.certificator.patron_ms.Exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

public class CertificateNotFoundException extends ResponseStatusException {
    public CertificateNotFoundException(String message) {
        super(HttpStatus.NOT_FOUND, message);
    }
}