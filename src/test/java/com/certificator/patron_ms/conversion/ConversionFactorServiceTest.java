package com.certificator.patron_ms.conversion;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.context.ActiveProfiles;

import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.certificate.CertificateRepository;
import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.ConversionResultDTO;
import com.certificator.patron_ms.conversion.dto.UncertaintyByPtnDTO;
import com.certificator.patron_ms.shared.Exception.CertificateNotFoundException;
import com.certificator.patron_ms.utils.TestDataFactory;

@ExtendWith(MockitoExtension.class)
@ActiveProfiles("test")
public class ConversionFactorServiceTest {

    @Mock CertificateRepository certificateRepository;
    @Mock UnitConversionService unitConversionService;
    @InjectMocks ConversionFactorService changeService;

    @Test
    void testGetPatronesByMeasure_returnsMatchingCertificates() {
        ConversionResponseDTO request = TestDataFactory.buildChangeRequest("temperatura", "C", 50.0);
        ConversionResultDTO conversion = TestDataFactory.buildConversionResult(50.0);
        Certificate dummyCert = TestDataFactory.buildCertificate("CERT-001");

        when(unitConversionService.convertUnits("Temperatura", "C", null, 50.0)).thenReturn(conversion);
        when(certificateRepository.findMatchingCertificates("Temperatura", 50.0)).thenReturn(List.of(dummyCert));

        List<Certificate> result = changeService.getPatronesByMeasure(request);

        assertEquals(1, result.size());
        assertEquals("CERT-001", result.get(0).getCertificateNumber());
        verify(certificateRepository, times(1)).findMatchingCertificates("Temperatura", 50.0);
    }

    @Test
    void testGetPatronesByMeasure_whenNoResults_returnsEmptyList() {
        ConversionResponseDTO request = TestDataFactory.buildChangeRequest("Presion", "bar", 1501.0);
        ConversionResultDTO conversion = TestDataFactory.buildConversionResult(101.0);

        when(unitConversionService.convertUnits("Presion", "bar", null, 1501.0)).thenReturn(conversion);
        when(certificateRepository.findMatchingCertificates("Presion", 101.0)).thenReturn(Collections.emptyList());

        List<Certificate> result = changeService.getPatronesByMeasure(request);

        assertEquals(0, result.size());
        verify(certificateRepository, times(1)).findMatchingCertificates("Presion", 101.0);
    }

    @Test
    void getUncertaintyByPtnS_HappyPath_ReturnsUncertainty() {
        UncertaintyByPtnDTO request = TestDataFactory.buildUncertaintyRequest("C", 34.3, "PTN-001");

        when(certificateRepository.findMagnitudByNameIdentify("PTN-001")).thenReturn("temperatura");
        when(unitConversionService.convertUnits("temperatura", "C", null, 34.3))
            .thenReturn(TestDataFactory.buildConversionResult(34.3));
        when(certificateRepository.findUncertaintyAboveReferenceByNameIdentify("PTN-001", 34.3))
            .thenReturn(Optional.of(0.05));

        Double result = changeService.getUncertaintyByPtnS(request);
        assertEquals(0.05, result);
    }

    @Test
    void getUncertaintyByPtnS_MagnitudNotFound_throwsException() {
        UncertaintyByPtnDTO request = TestDataFactory.buildUncertaintyRequest("C", 34.3, "PTN-999");
        when(certificateRepository.findMagnitudByNameIdentify("PTN-999")).thenReturn(null);

        CertificateNotFoundException ex = assertThrows(CertificateNotFoundException.class,
            () -> changeService.getUncertaintyByPtnS(request));
        assertEquals("No se encontró magnitud para el identificador: PTN-999", ex.getReason());
    }

    @Test
    void convert_ShouldReturnValidResponse_WhenConversionIsSuccessful() throws Exception {
        ConversionRequestDTO dto = TestDataFactory.buildValidChangeRequest();
        ConversionResultDTO result = TestDataFactory.buildConversionResult(34000.0);

        when(unitConversionService.convertUnits("presion", "bar", "mbar", 34.0)).thenReturn(result);

        ConversionResponseDTO response = changeService.convert(dto);

        assertEquals(34.0, response.getInputValue());
        assertEquals(34000.0, response.getOutputValue());
        assertEquals("bar", response.getInputUnit());
        assertEquals("mbar", response.getOutputUnit());
        assertEquals("presion", response.getMagnitud());
    }
}
