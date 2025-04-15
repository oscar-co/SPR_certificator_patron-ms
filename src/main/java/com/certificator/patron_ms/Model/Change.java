package com.certificator.patron_ms.Model;

public class Change {

    private String magnitud;
    private String inputUnit;
    private String outputUnit;
    private Double inputValue;
    private Double outputValue;

    // // Default constructor
    // public Change() {}

    // // All-args constructor
    // public Change(String magnitud, String inputUnit, String outputUnit, Double inputValue, Double outputValue) {
    //     this.inputUnit = inputUnit;
    //     this.outputUnit = outputUnit;
    //     this.inputValue = inputValue;
    //     this.outputValue = outputValue;
    // }

    public Change(String inputUnit, Double inputValue) {
        this.inputUnit = inputUnit;
        this.inputValue = inputValue;
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

    public String getMagnitud() {
        return magnitud;
    }

    public void setMagnitud(String magnitud) {
        this.magnitud = magnitud;
    }
}
