package com.certificator.patron_ms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Repository.CertificateRepository;
import com.certificator.patron_ms.Service.CertificateService;

@ExtendWith(MockitoExtension.class)
class CertificateServiceTest {

    @Mock
    private CertificateRepository certificateRepository;
    
    @InjectMocks
    private CertificateService certificateService;

    // @BeforeEach
    // void setUp() {

    // }


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
        
        // Verificamos que el repositorio fue llamado correctamente
        verify(certificateRepository).save(certificate);
    }


    @Test
    void testPostNewPatron_NullCertificate_ThrowsBadRequestException() {
        ResponseStatusException thrown = assertThrows(ResponseStatusException.class, () -> {
            certificateService.createNewPtn(null);
        });

        assertEquals(HttpStatus.BAD_REQUEST, thrown.getStatusCode());
        assertTrue(thrown.getReason().contains("Certificate cannot be null"));
    }
}