package com.certificator.patron_ms.conversion.dto;

public class ConversionResponseDTO {

    private String magnitud;
    private String inputUnit;
    private String outputUnit;
    private Double inputValue;
    private Double outputValue;

    public ConversionResponseDTO() {}

    public ConversionResponseDTO(String magnitud, String inputUnit, String outputUnit, Double inputValue, Double outputValue) {
        this.magnitud = magnitud;
        this.inputUnit = inputUnit;
        this.outputUnit = outputUnit;
        this.inputValue = inputValue;
        this.outputValue = outputValue;
    }

    public String getMagnitud() { return magnitud; }
    public void setMagnitud(String magnitud) { this.magnitud = magnitud; }

    public String getInputUnit() { return inputUnit; }
    public void setInputUnit(String inputUnit) { this.inputUnit = inputUnit; }

    public String getOutputUnit() { return outputUnit; }
    public void setOutputUnit(String outputUnit) { this.outputUnit = outputUnit; }

    public Double getInputValue() { return inputValue; }
    public void setInputValue(Double inputValue) { this.inputValue = inputValue; }

    public Double getOutputValue() { return outputValue; }
    public void setOutputValue(Double outputValue) { this.outputValue = outputValue; }
}
