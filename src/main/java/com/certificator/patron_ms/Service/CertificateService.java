package com.certificator.patron_ms.Service;

import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Repository.CertificateRepository;

@Service
public class CertificateService {

    
    //@Autowired 
    CertificateRepository certificateRepository;
    
    public CertificateService(CertificateRepository certificateRepository) {
        this.certificateRepository = certificateRepository;
    }

    public Certificate createNewPtn(Certificate certificate) {
        if (certificate == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Certificate cannot be null");
        }
        try {
            return certificateRepository.save(certificate);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.INTERNAL_SERVER_ERROR, "Error saving certificate: " + e.getMessage(), e);
        }
    }

    public Certificate getPtnById(Long id){
        return certificateRepository.findById(id)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found with id: " + id));
    }

    public List<Certificate> getAllCertificates(){
        return certificateRepository.findAll();
    }
}
