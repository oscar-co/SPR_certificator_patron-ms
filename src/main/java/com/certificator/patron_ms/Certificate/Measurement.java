package com.certificator.patron_ms.certificate;

import jakarta.persistence.*;

import java.math.BigDecimal;

import com.fasterxml.jackson.annotation.JsonBackReference;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(name = "certificate_id")
    @JsonBackReference
    private Certificate certificate;

    private String certificateNumber;
    private BigDecimal reference;
    private BigDecimal instrumentReading;
    private BigDecimal correction;
    private BigDecimal uncertainty;
    private BigDecimal ambientTemp;

    // Constructor vacío
    public Measurement() {}

    // Constructor completo
    public Measurement(Long id, Certificate certificate, String certificateNumber, BigDecimal reference,
                       BigDecimal instrumentReading, BigDecimal correction, BigDecimal uncertainty,
                       BigDecimal ambientTemp) {
        this.id = id;
        this.certificate = certificate;
        this.certificateNumber = certificateNumber;
        this.reference = reference;
        this.instrumentReading = instrumentReading;
        this.correction = correction;
        this.uncertainty = uncertainty;
        this.ambientTemp = ambientTemp;
    }

    public Long getId() { return id; }
    public void setId(Long id) { this.id = id; }

    public Certificate getCertificate() { return certificate; }
    public void setCertificate(Certificate certificate) { this.certificate = certificate; }

    public String getCertificateNumber() { return certificateNumber; }
    public void setCertificateNumber(String certificateNumber) { this.certificateNumber = certificateNumber; }

    public BigDecimal getReference() { return reference; }
    public void setReference(BigDecimal reference) { this.reference = reference; }

    public BigDecimal getInstrumentReading() { return instrumentReading; }
    public void setInstrumentReading(BigDecimal instrumentReading) { this.instrumentReading = instrumentReading; }

    public BigDecimal getCorrection() { return correction; }
    public void setCorrection(BigDecimal correction) { this.correction = correction; }

    public BigDecimal getUncertainty() { return uncertainty; }
    public void setUncertainty(BigDecimal uncertainty) { this.uncertainty = uncertainty; }

    public BigDecimal getAmbientTemp() { return ambientTemp; }
    public void setAmbientTemp(BigDecimal ambientTemp) { this.ambientTemp = ambientTemp; }
}
