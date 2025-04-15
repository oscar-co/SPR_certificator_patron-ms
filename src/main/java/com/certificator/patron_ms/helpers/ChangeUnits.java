package com.certificator.patron_ms.helpers;

import org.springframework.stereotype.Service;

@Service
public class ChangeUnits {


    public double calculoTemperatura(String eUnit, String sUnit, double eValue) {

        if (eUnit.equals("K") && sUnit.equals("C")) {
            return eValue - 273.15;
        } else if (eUnit.equals("F") && sUnit.equals("C")) {
            return (eValue - 32) * 5 / 9;
        } else if (eUnit.equals("C") && sUnit.equals("C")) {
            return eValue;
        } else {
            throw new UnsupportedOperationException("Unsupported temperature conversion from " + eUnit + " to " + sUnit);
        }
    }

    public double calculoMagnitudes(String eUnit, String sUnit, double eValue) {
        // Aquí deberías implementar lógica de conversión para otras magnitudes según unidades
        // Por ahora se devuelve el valor tal cual, suponiendo misma unidad
        if (eUnit.equals(sUnit)) {
            return eValue;
        }

        // Ejemplo: conversión simple
        if (eUnit.equals("kg") && sUnit.equals("g")) {
            return eValue * 1000;
        } else if (eUnit.equals("bar") && sUnit.equals("mbar")) {
            return eValue * 1000;
        }

        throw new UnsupportedOperationException("Unsupported conversion from " + eUnit + " to " + sUnit);
    }
}
