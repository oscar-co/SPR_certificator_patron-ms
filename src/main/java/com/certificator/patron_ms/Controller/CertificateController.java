package com.certificator.patron_ms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.certificator.patron_ms.DTO.ApiResponse;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Service.CertificateService;

import jakarta.validation.Valid;


@RestController
@RequestMapping("/api/patrones")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;


    @PostMapping
    public ResponseEntity<ApiResponse<Certificate>> createPtn(@Valid @RequestBody Certificate certificate){
        Certificate cert = certificateService.createNewPtn(certificate);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Ptn creado con exito", 
                cert )
        );    
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<Certificate>> getPtnById(@PathVariable Long id){
        Certificate cert = certificateService.getPtnById(id);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Ptn obtenido correctamente", 
                cert )
        );
    }

    @GetMapping
    public ResponseEntity<ApiResponse<List<Certificate>>> getAllCertificates(){
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Ptn obtenido correctamente", 
                certificateService.getAllCertificates() )
        );
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<ApiResponse<String>> deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificate(id);
        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Certificado eliminado correctamente", 
                null)
        );
    }

    @PutMapping("/{id}")
    public ResponseEntity<ApiResponse<Certificate>> updateCertificate(@PathVariable Long id, @Valid @RequestBody Certificate certificate){

        return ResponseEntity.ok(
            new ApiResponse<>(
                "success", 
                "Ptn actualizado correctamente", 
                certificateService.updateCertificateById(id, certificate) )
        );
    }
    
}