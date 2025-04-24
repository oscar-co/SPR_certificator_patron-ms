package com.certificator.patron_ms.ChangeTests;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Model.ConversionResult;
import com.certificator.patron_ms.Repository.CertificateRepository;
import com.certificator.patron_ms.Service.ChangeService;
import com.certificator.patron_ms.Service.UnitConversionService;

@ExtendWith(MockitoExtension.class)
public class ChangeServiceTest {

    @Mock
    CertificateRepository certificateRepository;

    @Mock
    UnitConversionService unitConversionService;

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
    void testGetPatronesByMeasure_incompleteJsonObject_returnsException() {
        // Arrange
        Change request = new Change();
        request.setMagnitud("temperatura");
        request.setInputUnit("C");
        //request.setInputValue(50.0);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            changeService.getPatronesByMeasure(request);
        });
        assertEquals("Faltan datos obligatorios en el cuerpo del JSON.", exception.getMessage());
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

    @Test
    void getUncertaintyByPtnS_HappyPath_ReturnsUncertainty() {
        UncertaintyByPtnDTO request = new UncertaintyByPtnDTO("C", 34.3, "PTN-001");

        when(certificateRepository.findMagnitudByNameIdentify("PTN-001")).thenReturn("temperatura");
        when(unitConversionService.convertToReferenceUnit("temperatura", "C", 34.3))
                .thenReturn(new ConversionResult(34.3, 34.3, "C"));
        when(certificateRepository.findUncertaintyAboveReferenceByNameIdentify("PTN-001", 34.3))
                .thenReturn(Optional.of(0.05));

        Double result = changeService.getUncertaintyByPtnS(request);

        assertEquals(0.05, result);
    }

    @Test
    void getUncertaintyByPtnS_MagnitudNotFound_returnsNull() {
        UncertaintyByPtnDTO request = new UncertaintyByPtnDTO("C", 34.3, "PTN-999");

        when(certificateRepository.findMagnitudByNameIdentify("PTN-999")).thenReturn(null);

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class, () -> {
            changeService.getUncertaintyByPtnS(request);
        });

        assertEquals("No se encontró magnitud para el identificador: PTN-999", exception.getMessage());
    }

    // @Test
    // void getUncertaintyByPtnS_ConversionFails_ThrowsException() {
    //     UncertaintyByPtnDTO request = new UncertaintyByPtnDTO("C", 34.3, "sensor123");

    //     when(certificateRepository.findMagnitudByNameIdentify("sensor123")).thenReturn("temperatura");
    //     when(unitConversionService.convertToReferenceUnit("temperatura", "C", 34.3))
    //             .thenThrow(new RuntimeException("Conversion error"));

    //     assertThrows(RuntimeException.class, () -> {
    //         changeService.getUncertaintyByPtnS(request);
    //     });
    // }

}
