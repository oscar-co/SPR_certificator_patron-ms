package com.certificator.patron_ms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.certificator.patron_ms.Model.ConversionResult;
import com.certificator.patron_ms.Service.UnitConversionService;

@SpringBootTest
public class ChangeUnitsTest {

    @Autowired
    private UnitConversionService unitConversionService;

    @Test
    void testConvertToReferenceUnit_Temperatura_FahrenheitToCelsius() {
        ConversionResult conversionResult = unitConversionService.convertToReferenceUnit("Temperatura", "F", 57.9);
        assertEquals("C", conversionResult.getUnit());
        assertEquals(14.38888888888889, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Temperatura_KelvinToCelsius() {
        ConversionResult conversionResult = unitConversionService.convertToReferenceUnit("Temperatura", "K", 293.15);
        assertEquals("C", conversionResult.getUnit());
        assertEquals(20.0, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Temperatura_CelsiusToCelsius() {
        ConversionResult conversionResult = unitConversionService.convertToReferenceUnit("Temperatura", "C", 37.15);
        assertEquals("C", conversionResult.getUnit());
        assertEquals(37.15, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Temperatura_WrongUnit_ToCelsius() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> {
                unitConversionService.convertToReferenceUnit("Temperatura", "X", 43d);
            }
        );
        assertEquals("Temperature unit not supported: X", exception.getMessage());
    }
}
