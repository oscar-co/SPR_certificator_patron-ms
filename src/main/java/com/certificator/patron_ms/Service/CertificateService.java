package com.certificator.patron_ms.Service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Repository.CertificateRepository;
import com.certificator.patron_ms.utils.CertificateValidator;

@Service
public class CertificateService {

    private static final Logger logger = LoggerFactory.getLogger(CertificateService.class);
    private final CertificateValidator certificateValidator;
    
    CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository repository, CertificateValidator certificateValidator) {
        this.certificateRepository = repository;
        this.certificateValidator = certificateValidator;
    }

    public Certificate createNewPtn(Certificate certificate) {
        certificateValidator.validate(certificate);
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


    public boolean deleteCertificate(Long id) {

        if (id == null) {
            logger.warn("El ID proporcionado es null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID cannot be null");
        }
        logger.debug("Intentando eliminar certificado con id: {}", id);
    
        if (!certificateRepository.existsById(id)) {
            logger.warn("Certificado no encontrado para eliminar con id: {}", id);
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found with id: " + id);
        }
        try {
            certificateRepository.deleteById(id);
            logger.info("Certificado eliminado correctamente con id: {}", id);
            return true;
        } catch (Exception e) {
            logger.error("Error al eliminar certificado con id: {}", id, e);
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error deleting certificate with id: " + id, e);
        }
    }


    public Certificate updateCertificateById(Long id, Certificate updatedCertificate){

        if (id == null) {
            logger.warn("El ID proporcionado es null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID cannot be null");
        }
        logger.debug("Intentando actualizar certificado con id: {}", id);
        
        Certificate oldCertificate = certificateRepository.findById(id)
        .orElseThrow(() -> {
            logger.warn("Certificado no encontrado para actualizar con id: {}", id);
            return new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found with id: " + id);
        });       
        updatedCertificate.setId(oldCertificate.getId());
        
        Certificate saved = certificateRepository.save(updatedCertificate);
        logger.info("Certificado actualizado con éxito. ID: {}", saved.getId());
        return saved;
    }
}
