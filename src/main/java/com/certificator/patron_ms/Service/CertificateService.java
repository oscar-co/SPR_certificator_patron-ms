package com.certificator.patron_ms.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Repository.CertificateRepository;

@Service
public class CertificateService {

    private static final Logger logger = LoggerFactory.getLogger(CertificateService.class);
    
    CertificateRepository certificateRepository;
    
    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public Certificate createNewPtn(Certificate certificate) {
        
        if (certificate == null) {
            logger.warn("Intento de crear un certificado nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Certificate cannot be null");
        }
        if (certificate.getNameIdentify() == null || certificate.getNameIdentify().isBlank()) {
            logger.warn("Intento de crear un certificado nulo");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Certificate name is required");
        }
        try {
            logger.info("Creando nuevo certificado: {}", certificate);
            return certificateRepository.save(certificate);
        } catch (Exception e) {
            logger.error("Error al guardar el certificado: {}", e.getMessage(), e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving certificate: " + e.getMessage(), e);
        }
    }

    public Certificate getPtnById(Long id){
        logger.debug("Buscando certificado con id: {}", id);
        return certificateRepository.findById(id)
            .orElseThrow( () -> {
                logger.warn("Certificado no encontrado con id: {}", id);
                return new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found with id: " + id);
            });
            
    }

    public List<Certificate> getAllCertificates(){
        logger.debug("getting All certificates");
        return certificateRepository.findAll();
    }
}
