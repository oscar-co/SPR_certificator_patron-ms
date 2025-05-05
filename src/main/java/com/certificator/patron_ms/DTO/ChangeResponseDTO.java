package com.certificator.patron_ms.DTO;

public class ChangeResponseDTO {

    private Double inputValue;
    private Double outputValue;
    private String inputUnit;
    private String outputUnit;
    private String magnitud;


    public ChangeResponseDTO() {
    }

    public ChangeResponseDTO(Double inputValue, Double outputValue, String inputUnit, String outputUnit, String magnitud) {
        this.inputValue = inputValue;
        this.outputValue = outputValue;
        this.inputUnit = inputUnit;
        this.outputUnit = outputUnit;
        this.magnitud = magnitud;
    }

    public Double getInputValue() {
        return inputValue;
    }

    public void setInputValue(Double inputValue) {
        this.inputValue = inputValue;
    }

    public Double getOutputValue() {
        return outputValue;
    }

    public void setOutputValue(Double outputValue) {
        this.outputValue = outputValue;
    }

    public String getInputUnit() {
        return inputUnit;
    }

    public void setInputUnit(String inputUnit) {
        this.inputUnit = inputUnit;
    }

    public String getOutputUnit() {
        return outputUnit;
    }

    public void setOutputUnit(String outputUnit) {
        this.outputUnit = outputUnit;
    }

    public String getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }
}