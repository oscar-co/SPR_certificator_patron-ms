package com.certificator.patron_ms;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Repository.CertificateRepository;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.extension.ExtendWith;
import static org.mockito.Mockito.*;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.jupiter.MockitoExtension;

import com.certificator.patron_ms.Service.CertificateService;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;
    
    @InjectMocks
    private CertificateService certificateService;

    @BeforeEach
    void setUp() {

    }

    @Test
    void testGetPatronesByMeasure_returnsMatchingCertificates() {
        // Arrange
        Change request = new Change();
        request.setMagnitud("temperatura");
        request.setInputValue(50.0);

        Certificate dummyCert = new Certificate();
        dummyCert.setCertificateNumber("CERT-001");

        List<Certificate> expected = List.of(dummyCert);

        when(certificateRepository.findMatchingCertificates("Temperatura", 50.0)).thenReturn(expected);

        // Act
        List<Certificate> result = certificateService.getPatronesByMeasure(request);

        // Assert
        assertEquals(1, result.size());
        assertEquals("CERT-001", result.get(0).getCertificateNumber());
        verify(certificateRepository, times(1)).findMatchingCertificates("Temperatura", 50.0);
    }

    @Test
    void testGetPatronesByMeasure_whenNoResults_returnsEmptyList() {
        // Arrange
        Change request = new Change();
        request.setMagnitud("presión");
        request.setInputValue(101.0);

        when(certificateRepository.findMatchingCertificates("Presión", 101.0)).thenReturn(Collections.emptyList());

        // Act
        List<Certificate> result = certificateService.getPatronesByMeasure(request);

        // Assert
        assertEquals(0, result.size());
        verify(certificateRepository, times(1)).findMatchingCertificates("Presión", 101.0);
    }
}