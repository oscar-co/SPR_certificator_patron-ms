package com.certificator.patron_ms.certificate;

import java.io.IOException;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Profile;
import org.springframework.core.io.ClassPathResource;
import org.springframework.stereotype.Component;

import java.io.InputStream;
import java.util.List;

@Component
@Profile("!test")
public class DataInitializer implements CommandLineRunner {

    @Autowired private CertificateService certificateService;

    @Autowired private ObjectMapper objectMapper;

    @Autowired private CertificateRepository certificateRepository;
    


    @Override
    public void run(String... args) throws Exception {
        try {

            certificateRepository.deleteAll();

            ClassPathResource resource = new ClassPathResource("data/certificate-with-measurements.json");
            InputStream inputStream = resource.getInputStream();

            List<Certificate> certificates = objectMapper.readValue(
                inputStream, 
                new com.fasterxml.jackson.core.type.TypeReference<List<Certificate>>() {}
            );
            
            for (Certificate cert : certificates) {
                certificateService.createNewPtn(cert);
            }

            System.out.println("Certificates loaded successfully on startup.");
        } catch (IOException e) {
            System.err.println("Error loading certificate data: " + e.getMessage());
        }
    }
}
