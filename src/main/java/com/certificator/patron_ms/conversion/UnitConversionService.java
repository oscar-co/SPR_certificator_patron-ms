package com.certificator.patron_ms.conversion;

import org.springframework.stereotype.Service;

import com.certificator.patron_ms.ENUM.AreaUnit;
import com.certificator.patron_ms.ENUM.LongitudeUnit;
import com.certificator.patron_ms.ENUM.MagnitudeType;
import com.certificator.patron_ms.ENUM.MassUnit;
import com.certificator.patron_ms.ENUM.PressureUnit;
import com.certificator.patron_ms.ENUM.TemperatureUnit;
import com.certificator.patron_ms.conversion.dto.ConversionResultDTO;
import com.certificator.patron_ms.utils.Utils;

@Service
public class UnitConversionService {

    private final ConversionFactorRepository factorRepository;

    public UnitConversionService(ConversionFactorRepository factorRepository) {
        this.factorRepository = factorRepository;
    }

    public Double calculoMagnitudes(String uEntrada, String uSalida, double vEntrada) {
        if(uEntrada.equalsIgnoreCase(uSalida)) return vEntrada;
        ConversionFactor factor = factorRepository.findByUEntradaAndUSalida(uEntrada, uSalida)
            .orElseThrow(() -> new IllegalArgumentException("Factor no encontrado para " + uEntrada + " → " + uSalida));

        return vEntrada * factor.getFactor();
    }

    public static Double calculoTemperatura(String iUnit, String oUnit, Double eValue) {
        iUnit = iUnit.toUpperCase();
        oUnit = oUnit.toUpperCase();
    
        if (iUnit.equals(oUnit)) return eValue;
    
        // Paso intermedio: convertir a Celsius
        double tempC;
        switch (iUnit) {
            case "C":
                tempC = eValue;
                break;
            case "F":
                tempC = (eValue - 32) * 5.0 / 9.0;
                break;
            case "K":
                tempC = eValue - 273.15;
                break;
            default:
                throw new UnsupportedOperationException("Unsupported temperature conversion from: " + iUnit + " to " + oUnit);
        }
    
        // Convertir de Celsius a unidad de salida
        switch (oUnit) {
            case "C":
                return tempC;
            case "F":
                return tempC * 9.0 / 5.0 + 32;
            case "K":
                return tempC + 273.15;
            default:
                throw new UnsupportedOperationException("Unidad de salida no soportada: " + oUnit);
        }
    }

    /**
     * Convierte una magnitud de una unidad a otra.
     *
     * @param magnitud   La magnitud a convertir (temperatura, presión, masa, longitud, área).
     * @param inputUnit  La unidad de entrada.
     * @param outputUnit La unidad de salida.
     * @param value      El valor a convertir.
     * @return Un objeto ConversionResultDTO con el valor convertido y la unidad de salida.
     */
    public ConversionResultDTO convertUnits(String magnitud, String inputUnit, String outputUnit, Double value) {


        Double originalValue = value;
        MagnitudeType magnitudeType = MagnitudeType.fromString(magnitud);
        ConversionResultDTO result = new ConversionResultDTO();
    
        // Si no se especifica unidad de salida, utilizo la de referencia
        if (outputUnit == null || outputUnit.isBlank()) {
            outputUnit = Utils.getDefaultReferenceUnit(magnitudeType);
        }
    
        switch (magnitudeType) {
            case TEMPERATURA:
                TemperatureUnit.fromString(inputUnit);
                TemperatureUnit.fromString(outputUnit);

                result.setUnit(outputUnit);
                result.setConvertedValue(calculoTemperatura(inputUnit, outputUnit, value));
                break;
    
            case PRESION:
                PressureUnit.fromString(inputUnit);
                PressureUnit.fromString(outputUnit);

                result.setUnit(outputUnit);
                result.setConvertedValue(calculoMagnitudes(inputUnit, outputUnit, value));
                break;

            case MASA:
                MassUnit.fromString(inputUnit);
                MassUnit.fromString(outputUnit);

                result.setUnit(outputUnit);
                result.setConvertedValue(calculoMagnitudes(inputUnit, outputUnit, value));
                break;

            case LONGITUD:
                LongitudeUnit.fromString(inputUnit);
                LongitudeUnit.fromString(outputUnit);

                result.setUnit(outputUnit);
                result.setConvertedValue(calculoMagnitudes(inputUnit, outputUnit, value));
                break;

            case AREA:
                AreaUnit.fromString(inputUnit);
                AreaUnit.fromString(outputUnit);

                result.setUnit(outputUnit);
                result.setConvertedValue(calculoMagnitudes(inputUnit, outputUnit, value));
                break;
    
            default:
                throw new IllegalArgumentException("Magnitud no soportada: " + magnitud);
        }
        result.setOriginalValue(originalValue);
        return result;
    }
}