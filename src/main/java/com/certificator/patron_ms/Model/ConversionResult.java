package com.certificator.patron_ms.Model;


public class ConversionResult {
    
    private Double originalValue;
    private Double convertedValue;
    private String unit;

    public ConversionResult(Double convertedValue, Double originalValue, String unit) {
        this.convertedValue = convertedValue;
        this.originalValue = originalValue;
        this.unit = unit;
    }

    public ConversionResult() {
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
