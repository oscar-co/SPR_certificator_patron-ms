package com.certificator.patron_ms.Service;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.server.ResponseStatusException;

import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Repository.CertificateRepository;

@Service
public class CertificateService {

    @Autowired CertificateRepository certificateRepository;


    public Certificate createNewPtn(Certificate certificate) {
        return certificateRepository.save(certificate);
    }

    public Certificate getPtnById(Long id){
        return certificateRepository.findById(id)
            .orElseThrow( () -> new ResponseStatusException(HttpStatus.NOT_FOUND, "Certificate not found with id: " + id));
    }

    public List<Certificate> getAllCertificates(){
        return certificateRepository.findAll();
    }

    public List<Certificate> getPatronesByMeasure(Change request) {

        if (request.getInputValue() == null || request.getMagnitud() == null) {
            throw new IllegalArgumentException("Faltan datos obligatorios en el cuerpo del JSON.");
        }
        
        return certificateRepository.findMatchingCertificates(
            request.getMagnitud(),   
            request.getInputValue()
        );
    }

    
}
