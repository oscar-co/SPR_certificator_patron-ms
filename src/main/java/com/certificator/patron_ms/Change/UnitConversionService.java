package com.certificator.patron_ms.Change;

import org.springframework.stereotype.Service;

import com.certificator.patron_ms.ENUM.MagnitudeType;
import com.certificator.patron_ms.ENUM.MassUnit;
import com.certificator.patron_ms.ENUM.PressureUnit;
import com.certificator.patron_ms.ENUM.TemperatureUnit;

@Service
public class UnitConversionService {

    private final ConversionFactorRepository factorRepository;

    public UnitConversionService(ConversionFactorRepository factorRepository) {
        this.factorRepository = factorRepository;
    }

    public Double calculoMagnitudes(String uEntrada, String uSalida, double vEntrada) {
        ConversionFactor factor = factorRepository.findByUEntradaAndUSalida(uEntrada, uSalida)
            .orElseThrow(() -> new IllegalArgumentException("Factor no encontrado para " + uEntrada + " → " + uSalida));

        return vEntrada * factor.getFactor();
    }

    // Posiblemente lo vaya a necesitar para hacer simples conbersiones de tª
    // public static double calculoTemperatura(String eUnit, String sUnit, double eValue) {

    //     if (!TemperatureUnit.isValid(eUnit) || !TemperatureUnit.isValid(sUnit)) {
    //         throw new UnsupportedOperationException("Conversión de temperatura no soportada: " + eUnit + " → " + sUnit);
    //     }

    //     if (eUnit.equals("K") && sUnit.equals("C")) {
    //         return eValue - 273.15;
    //     } else if (eUnit.equals("F") && sUnit.equals("C")) {
    //         return (eValue - 32) * 5 / 9;
    //     } else if (eUnit.equals("C") && sUnit.equals("C")) {
    //         return eValue;
    //     } else {
    //         throw new UnsupportedOperationException("Unsupported temperature conversion from " + eUnit + " to " + sUnit);
    //     }
    // }

    public static Double calculoTemperatura(String eUnit, Double eValue) {

        String sUnit = "C";
        if (eUnit.equals("K")) {
            return eValue - 273.15;
        } else if (eUnit.equals("F")) {
            return (eValue - 32) * 5 / 9;
        } else if (eUnit.equals("C")) {
            return eValue;
        } else {
            throw new UnsupportedOperationException("Unsupported temperature conversion from " + eUnit + " to " + sUnit);
        }
    }

    public ConversionResult convertToReferenceUnit(String magnitud, String unit, Double value){

        Double originalValue = value;
        MagnitudeType magnitudeType = MagnitudeType.fromString(magnitud);
        ConversionResult result = new ConversionResult();

        switch (magnitudeType) {
            case TEMPERATURA:
                if (!TemperatureUnit.isValid(unit)) {
                    throw new IllegalArgumentException("Temperature unit not supported: " + unit);
                }
                result.setUnit("C");
                result.setConvertedValue(calculoTemperatura(unit, value));
            break;
        
            case PRESION:
                if (!PressureUnit.isValid(unit)) {
                    throw new IllegalArgumentException("Pressure unit not supported: " + unit);
                }
                result.setUnit("bar");
                result.setConvertedValue(calculoMagnitudes(unit, "bar", value));
            break;

            // TODO se queda tb pendiente de meter los factores de cambio de masa
            // case MASA:
            //     if (!MassUnit.isValid(unit)) {
            //         throw new IllegalArgumentException("Mass unit not supported: " + unit);
            //     }
            //     result.setUnit("g");
            //     result.setConvertedValue(calculoMagnitudes(unit, "g", value));
            // break;
        
            // TODO
            default:
                throw new IllegalArgumentException("Magnitud no soportada: " + magnitud);
        }
        result.setOriginalValue(originalValue);
        return result;
    }
}