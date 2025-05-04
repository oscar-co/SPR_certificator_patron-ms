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

import com.certificator.patron_ms.Certificate.Certificate;
import com.certificator.patron_ms.Certificate.CertificateRepository;
import com.certificator.patron_ms.Change.Change;
import com.certificator.patron_ms.Change.ChangeService;
import com.certificator.patron_ms.Change.ConversionResult;
import com.certificator.patron_ms.Change.UnitConversionService;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Exception.CertificateNotFoundException;

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

        // Simular respuesta del conversion service
        ConversionResult conversionResult = new ConversionResult();
        conversionResult.setConvertedValue(50.0);

        when(unitConversionService.convertToReferenceUnit("Temperatura", "C", 50.0)).thenReturn(conversionResult);

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
        Change request = new Change();
        request.setMagnitud("Presion");
        request.setInputUnit("bar");
        request.setInputValue(1501.0);

        when(certificateRepository.findMatchingCertificates("Presion", 101.0))
            .thenReturn(Collections.emptyList());

        ConversionResult conversionResult = new ConversionResult();
        conversionResult.setConvertedValue(101.0); // Resultado que se usará para buscar

        when(unitConversionService.convertToReferenceUnit("Presion", "bar", 1501.0))
            .thenReturn(conversionResult);

        List<Certificate> result = changeService.getPatronesByMeasure(request);

        assertEquals(0, result.size());
        verify(certificateRepository, times(1)).findMatchingCertificates("Presion", 101.0);
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

        CertificateNotFoundException exception = assertThrows(CertificateNotFoundException.class, () -> {
            changeService.getUncertaintyByPtnS(request);
        });

        assertEquals("No se encontró magnitud para el identificador: PTN-999", exception.getReason());
    }
}
