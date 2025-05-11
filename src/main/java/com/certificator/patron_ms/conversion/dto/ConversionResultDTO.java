package com.certificator.patron_ms.conversion.dto;


public class ConversionResultDTO {
    
    private Double originalValue;
    private Double convertedValue;
    private String unit;

    public ConversionResultDTO(Double convertedValue, Double originalValue, String unit) {
        this.convertedValue = convertedValue;
        this.originalValue = originalValue;
        this.unit = unit;
    }

    public ConversionResultDTO() {
    }

    public Double getOriginalValue() {
        return originalValue;
    }

    public void setOriginalValue(Double originalValue) {
        this.originalValue = originalValue;
    }

    public Double getConvertedValue() {
        return convertedValue;
    }

    public void setConvertedValue(Double convertedValue) {
        this.convertedValue = convertedValue;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }
}
