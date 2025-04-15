package com.certificator.patron_ms.Model;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.OneToMany;

@Entity
public class Certificate {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certificateNumber;
    private String insType;
    private String brand;
    private String model;
    private String nameIdentify;
    private String description;
    private String unit;
    private LocalDate issueDate;

    @OneToMany(cascade = CascadeType.ALL, orphanRemoval = true)
    @JoinColumn(name = "certificate_id")
    private List<Measurement> measurements = new ArrayList<>();

    
    public Certificate(Long id, String certificateNumber, String insType, String brand, String model,
            String nameIdentify, String description, String unit, LocalDate issueDate, List<Measurement> measurements) {
        this.id = id;
        this.certificateNumber = certificateNumber;
        this.insType = insType;
        this.brand = brand;
        this.model = model;
        this.nameIdentify = nameIdentify;
        this.description = description;
        this.unit = unit;
        this.issueDate = issueDate;
        this.measurements = measurements;
    }

    public Certificate() {
    }

    public List<Measurement> getMeasurements() {
        return measurements;
    }
    
    public void setMeasurements(List<Measurement> measurements) {
        this.measurements = measurements;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getCertificateNumber() {
        return certificateNumber;
    }

    public void setCertificateNumber(String certificateNumber) {
        this.certificateNumber = certificateNumber;
    }

    public String getInsType() {
        return insType;
    }

    public void setInsType(String insType) {
        this.insType = insType;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getNameIdentify() {
        return nameIdentify;
    }

    public void setNameIdentify(String nameIdentify) {
        this.nameIdentify = nameIdentify;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public LocalDate getIssueDate() {
        return issueDate;
    }

    public void setIssueDate(LocalDate issueDate) {
        this.issueDate = issueDate;
    }  

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
