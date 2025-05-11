package com.certificator.patron_ms.certificate;

import java.math.BigDecimal;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class Measurement {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String certId;
    private BigDecimal reference;
    private BigDecimal instrumentReading;
    private BigDecimal correction;
    private BigDecimal uncertainty;
    private BigDecimal ambientTemp;

    // Getters y setters
    public Long getId() {
        return id;
    }
    public void setId(Long id) {
        this.id = id;
    }

    public String getCertId() {
        return certId;
    }
    public void setCertId(String certId) {
        this.certId = certId;
    }

    public BigDecimal getReference() {
        return reference;
    }
    public void setReference(BigDecimal reference) {
        this.reference = reference;
    }

    public BigDecimal getInstrumentReading() {
        return instrumentReading;
    }
    public void setInstrumentReading(BigDecimal instrumentReading) {
        this.instrumentReading = instrumentReading;
    }

    public BigDecimal getCorrection() {
        return correction;
    }
    public void setCorrection(BigDecimal correction) {
        this.correction = correction;
    }

    public BigDecimal getUncertainty() {
        return uncertainty;
    }
    public void setUncertainty(BigDecimal uncertainty) {
        this.uncertainty = uncertainty;
    }

    public BigDecimal getAmbientTemp() {
        return ambientTemp;
    }
    public void setAmbientTemp(BigDecimal ambientTemp) {
        this.ambientTemp = ambientTemp;
    }
}
