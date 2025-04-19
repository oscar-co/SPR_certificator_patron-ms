package com.certificator.patron_ms;

import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Repository.CertificateRepository;
import com.certificator.patron_ms.Service.ChangeService;

@ExtendWith(MockitoExtension.class)
public class ChangeServiceTest {

    @Mock
    CertificateRepository certificateRepository;

    @InjectMocks
    ChangeService changeService;

    @Test
    void testGetPatronesByMeasure_returnsMatchingCertificates() {
        // Arrange
        Change request = new Change();
        request.setMagnitud("temperatura");
        request.setInputUnit("C");
        request.setInputValue(50.0);

        Certificate dummyCert = new Certificate();
        dummyCert.setCertificateNumber("CERT-001");

        List<Certificate> expected = List.of(dummyCert);

        when(certificateRepository.findMatchingCertificates("Temperatura", 50.0)).thenReturn(expected);

        List<Certificate> result = changeService.getPatronesByMeasure(request);

        assertEquals(1, result.size());
        assertEquals("CERT-001", result.get(0).getCertificateNumber());
        verify(certificateRepository, times(1)).findMatchingCertificates("Temperatura", 50.0);
    }


    @Test
    void testGetPatronesByMeasure_whenNoResults_returnsEmptyList() {
        // Arrange
        Change request = new Change();
        request.setMagnitud("presión");
        request.setInputUnit("C");
        request.setInputValue(101.0);

        when(certificateRepository.findMatchingCertificates("Presión", 101.0)).thenReturn(Collections.emptyList());

        // Act
        List<Certificate> result = changeService.getPatronesByMeasure(request);

        // Assert
        assertEquals(0, result.size());
        verify(certificateRepository, times(1)).findMatchingCertificates("Presión", 101.0);
    }
}
