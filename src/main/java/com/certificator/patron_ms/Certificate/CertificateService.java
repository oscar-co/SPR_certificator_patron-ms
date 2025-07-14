package com.certificator.patron_ms.certificate;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.shared.Exception.CertificateNotFoundException;

@Service
public class CertificateService {

    private static final Logger logger = LoggerFactory.getLogger(CertificateService.class);
    
    CertificateRepository certificateRepository;

    public CertificateService(CertificateRepository repository) {
        this.certificateRepository = repository;
    }

    public Certificate createNewPtn(Certificate certificate) {
        logger.info("Creando nuevo certificado: {}", certificate);
        
        if (certificate.getMeasurements() != null) {
            for (Measurement m : certificate.getMeasurements()) {
                m.setCertificate(certificate);
            }
        }
        return certificateRepository.save(certificate);
    }

    public Certificate getPtnById(Long id) {
        if (id == null) { 
            throw new IllegalArgumentException("ID del certificado no puede ser null"); 
        }
    
        logger.debug("Buscando certificado con id: {}", id);
        return certificateRepository.findById(id)
            .orElseThrow(() -> new CertificateNotFoundException("Certificate not found with id: " + id));
    }

    public List<Certificate> getAllCertificates() {
        logger.debug("getting All certificates");
        List<Certificate> certs = certificateRepository.findAll();
        if (certs.isEmpty()) {
            logger.warn("No se encontraron certificados en la base de datos");
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "No hay certificados registrados");
        }
        return certs;
    }

    public void deleteCertificate(Long id) {
        if (id == null) {
            logger.warn("El ID proporcionado es null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "ID cannot be null");
        }
    
        logger.debug("Intentando eliminar certificado con id: {}", id);
    
        if (!certificateRepository.existsById(id)) {
            logger.warn("Certificado no encontrado para eliminar con id: {}", id);
            throw new CertificateNotFoundException("No se encontró certificado con ID: " + id);
        }
    
        certificateRepository.deleteById(id);
        logger.info("Certificado eliminado correctamente con id: {}", id);
    }

    public Certificate updateCertificateById(Long id, Certificate updatedCertificate) {
        if (id == null) {
            logger.warn("El ID proporcionado es null");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "El ID no puede ser null");
        }

        logger.debug("Intentando actualizar certificado con id: {}", id);

        Certificate oldCertificate = certificateRepository.findById(id)
            .orElseThrow(() -> {
                logger.warn("Certificado no encontrado con id: {}", id);
                return new CertificateNotFoundException("No se encontró certificado con ID: " + id);
            });

        // Actualiza los campos del certificado
        oldCertificate.setCertificateNumber(updatedCertificate.getCertificateNumber());
        oldCertificate.setInsType(updatedCertificate.getInsType());
        oldCertificate.setBrand(updatedCertificate.getBrand());
        oldCertificate.setModel(updatedCertificate.getModel());
        oldCertificate.setNameIdentify(updatedCertificate.getNameIdentify());
        oldCertificate.setDescription(updatedCertificate.getDescription());
        oldCertificate.setUnit(updatedCertificate.getUnit());
        oldCertificate.setIssueDate(updatedCertificate.getIssueDate());

        // Reemplazar mediciones anteriores por las nuevas
        oldCertificate.getMeasurements().clear();
        if (updatedCertificate.getMeasurements() != null) {
            for (Measurement m : updatedCertificate.getMeasurements()) {
                m.setCertificate(oldCertificate);
            }
            oldCertificate.getMeasurements().addAll(updatedCertificate.getMeasurements());
        }

        Certificate saved = certificateRepository.save(oldCertificate);
        logger.info("Certificado actualizado con éxito. ID: {}", saved.getId());
        return saved;
    }
}