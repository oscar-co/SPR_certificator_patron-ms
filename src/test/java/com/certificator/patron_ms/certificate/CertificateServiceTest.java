package com.certificator.patron_ms.certificate;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doThrow;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.shared.Exception.CertificateNotFoundException;
import com.certificator.patron_ms.shared.utils.CertificateValidator;

@ActiveProfiles("test")
@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;

    @Mock
    private CertificateValidator certificateValidator;
    
    @InjectMocks
    private CertificateService certificateService;

    @Test
    void testCreateNewPtn_Success() {
        // Datos de prueba
        Certificate certificate = new Certificate();
        certificate.setCertificateNumber("CERT-001");
        certificate.setNameIdentify("PTN-001");
        
        when(certificateRepository.save(certificate)).thenReturn(certificate);
        Certificate result = certificateService.createNewPtn(certificate);

        assertNotNull(result);
        assertEquals(certificate, result);
        
        verify(certificateRepository).save(certificate);
    }

    @Test
    void deleteCertificate_ExistingId_DeletesSuccessfully() {

        Long id = 1L;
        when(certificateRepository.existsById(id)).thenReturn(true);
        certificateService.deleteCertificate(id);

        verify(certificateRepository).existsById(id);
        verify(certificateRepository).deleteById(id);
    }

    @Test
    void deleteCertificate_NonExistingId_ThrowsNotFound() {
        Long id = 7L;
        when(certificateRepository.existsById(id)).thenReturn(false);

        CertificateNotFoundException exception = assertThrows(CertificateNotFoundException.class, () -> {
            certificateService.deleteCertificate(id);
        });

        assertEquals(HttpStatus.NOT_FOUND, exception.getStatusCode());
        String reason = exception.getReason();
        assertNotNull(reason);
        assertTrue(reason.contains("No se encontró certificado con ID: 7"));
        verify(certificateRepository, never()).deleteById(id);
    }

    @Test
    void deleteCertificate_DeleteFails_ThrowsInternalServerError() {
        Long id = 1L;

        when(certificateRepository.existsById(id)).thenReturn(true);

        // Simulamos que deleteById lanza una excepción inesperada
        doThrow(new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR,"DB error")).when(certificateRepository).deleteById(id);

        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificateService.deleteCertificate(id);
        });

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, exception.getStatusCode());
        String reason = exception.getReason();
        assertNotNull(reason);
        assertTrue(reason.contains("DB error"));
    }

    @Test
    void deleteCertificate_NullId_ThrowsBadRequest() {
        // Ejecutamos con null
        ResponseStatusException exception = assertThrows(ResponseStatusException.class, () -> {
            certificateService.deleteCertificate(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, exception.getStatusCode());
        String reason = exception.getReason();
        assertNotNull(reason);
        assertTrue(reason.contains("ID cannot be null"));
    }

    @Test
    void testUpdateCertificateById_successfulUpdate() {
        Long id = 1L;
        Certificate oldCertificate = new Certificate();
        oldCertificate.setId(id);
        oldCertificate.setCertificateNumber("CERT-001");

        Certificate updated = new Certificate();
        updated.setCertificateNumber("CERT-002");
        updated.setBrand("BrandX");
        updated.setModel("ModelY");
        updated.setIssueDate(LocalDate.now());

        Certificate saved = new Certificate();
        saved.setId(id);
        saved.setCertificateNumber("CERT-002");
        saved.setBrand("BrandX");
        saved.setModel("ModelY");
        saved.setIssueDate(updated.getIssueDate());

        when(certificateRepository.findById(id)).thenReturn(Optional.of(oldCertificate));
        when(certificateRepository.save(updated)).thenReturn(saved);

        Certificate result = certificateService.updateCertificateById(id, updated);

        assertNotNull(result);
        assertEquals(id, result.getId());
        assertEquals("CERT-002", result.getCertificateNumber());
        assertEquals("BrandX", result.getBrand());
        assertEquals("ModelY", result.getModel());
    }

    @Test
    void testUpdateCertificateById_nullId_shouldThrowBadRequest() {
        Certificate cert = new Certificate();
        cert.setCertificateNumber("abc");

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class, () -> certificateService.updateCertificateById(null, cert)
        );

        assertEquals(HttpStatus.BAD_REQUEST, ex.getStatusCode());
    }

    @Test
    void testUpdateCertificateById_notFound_shouldThrowNotFound() {
        Long id = 99L;
        Certificate cert = new Certificate();
        cert.setCertificateNumber("abc99");

        when(certificateRepository.findById(id)).thenReturn(Optional.empty());

        ResponseStatusException ex = assertThrows(
            ResponseStatusException.class,
            () -> certificateService.updateCertificateById(id, cert)
        );

        assertEquals(HttpStatus.NOT_FOUND, ex.getStatusCode());
    }

    @Test
    void testUpdateCertificateById_preservesId() {
        Long id = 42L;

        Certificate original = new Certificate();
        original.setId(id);
        original.setCertificateNumber("CERT-OLD");

        Certificate incoming = new Certificate();
        incoming.setCertificateNumber("CERT-NEW");

        when(certificateRepository.findById(id)).thenReturn(Optional.of(original));
        when(certificateRepository.save(any(Certificate.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        Certificate result = certificateService.updateCertificateById(id, incoming);

        assertEquals(id, result.getId());
        assertEquals("CERT-NEW", result.getCertificateNumber());
    }

    //TODO analizar bien este test porque lo copie de gpt pero no lo adapte ni comprendi
    @Test
    void testUpdateCertificateById_updatesRealMeasurementFields() {
        Long id = 123L;

        // Original Certificate con una medición vieja
        Certificate original = new Certificate();
        original.setId(id);
        Measurement oldMeasurement = new Measurement();
        oldMeasurement.setId(1L);
        oldMeasurement.setCertId("CERT-123");
        oldMeasurement.setReference(BigDecimal.valueOf(10.0));
        oldMeasurement.setInstrumentReading(BigDecimal.valueOf(9.8));
        oldMeasurement.setCorrection(BigDecimal.valueOf(0.2));
        oldMeasurement.setUncertainty(BigDecimal.valueOf(0.05));
        oldMeasurement.setAmbientTemp(BigDecimal.valueOf(23.0));
        original.setMeasurements(List.of(oldMeasurement));

        // Incoming Certificate con una nueva medición
        Certificate incoming = new Certificate();
        Measurement newMeasurement = new Measurement();
        newMeasurement.setId(2L);
        newMeasurement.setCertId("CERT-123");
        newMeasurement.setReference(BigDecimal.valueOf(20.0));
        newMeasurement.setInstrumentReading(BigDecimal.valueOf(19.9));
        newMeasurement.setCorrection(BigDecimal.valueOf(0.1));
        newMeasurement.setUncertainty(BigDecimal.valueOf(0.03));
        newMeasurement.setAmbientTemp(BigDecimal.valueOf(22.0));
        incoming.setMeasurements(List.of(newMeasurement));

        // Mockeamos comportamiento del repositorio
        when(certificateRepository.findById(id)).thenReturn(Optional.of(original));
        when(certificateRepository.save(any(Certificate.class)))
            .thenAnswer(invocation -> invocation.getArgument(0));

        // Ejecutamos el método
        Certificate result = certificateService.updateCertificateById(id, incoming);

        // Aserciones
        assertNotNull(result);
        assertEquals(1, result.getMeasurements().size());

        Measurement resultMeasurement = result.getMeasurements().get(0);
        assertEquals(BigDecimal.valueOf(20.0), resultMeasurement.getReference());
        assertEquals(BigDecimal.valueOf(19.9), resultMeasurement.getInstrumentReading());
        assertEquals(BigDecimal.valueOf(0.1), resultMeasurement.getCorrection());
        assertEquals(BigDecimal.valueOf(0.03), resultMeasurement.getUncertainty());
        assertEquals(BigDecimal.valueOf(22.0), resultMeasurement.getAmbientTemp());
    }
}