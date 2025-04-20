package com.certificator.patron_ms.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Service.CertificateService;
import com.certificator.patron_ms.Service.ChangeService;

@RestController
@RequestMapping("/api/patrones")
public class CertificateController {

    @Autowired
    private CertificateService certificateService;

    @Autowired
    private ChangeService changeService;

    @PostMapping
    public ResponseEntity<Certificate> createPtn(@RequestBody Certificate certificate){
        Certificate cert = certificateService.createNewPtn(certificate);
        return ResponseEntity.ok(cert);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Certificate> getPtnById(@PathVariable Long id){
        return ResponseEntity.ok(certificateService.getPtnById(id));
    }

    @GetMapping
    public ResponseEntity<List<Certificate>> getAllCertificates(){
        return ResponseEntity.ok(certificateService.getAllCertificates());
    }

    @PostMapping("/patrones-disponibles")
    public ResponseEntity<List<Certificate>> getPatronAvailable(@RequestBody Change change){

        return ResponseEntity.ok(changeService.getPatronesByMeasure(change));
    }

    @PostMapping("/incertidumbre-patron")
    public ResponseEntity<Double> getUncertaintyByPtn(@RequestBody UncertaintyByPtnDTO request){

        return ResponseEntity.ok(changeService.getUncertaintyByPtnS(request));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<String> deleteCertificate(@PathVariable Long id) {
        certificateService.deleteCertificate(id);
        return ResponseEntity.ok("Certificado eliminado correctamente");
    }

    @PutMapping("/{id}")
    public ResponseEntity<Certificate> updateCertificate(@PathVariable Long id, @RequestBody Certificate certificate){

        return ResponseEntity.ok(certificateService.updateCertificateById(id, certificate));
    }
}