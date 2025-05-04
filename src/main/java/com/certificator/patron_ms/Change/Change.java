package com.certificator.patron_ms.Change;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

public class Change {

    @NotBlank(message = "La magnitud es obligatoria")
    private String magnitud;

    @NotBlank(message = "La unidad de entrada es obligatoria")
    private String inputUnit;

    private String outputUnit;

    @NotNull(message = "El valor de entrada es obligatorio")
    private Double inputValue;

    private Double outputValue;

    // // Default constructor
    public Change() {}


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
