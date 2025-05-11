package com.certificator.patron_ms.ChangeTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.certificator.patron_ms.conversion.UnitConversionService;
import com.certificator.patron_ms.conversion.dto.ConversionResultDTO;

@SpringBootTest
public class ChangeUnitsTest {

    @Autowired
    private UnitConversionService unitConversionService;

    @Test
    void testConvertToReferenceUnit_Temperatura_FahrenheitToCelsius() {
        ConversionResultDTO conversionResult = unitConversionService.convertUnits("Temperatura", "F", null, 57.9);
        assertEquals("C", conversionResult.getUnit());
        assertEquals(14.38888888888889, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Temperatura_KelvinToCelsius() {
        ConversionResultDTO conversionResult = unitConversionService.convertUnits("Temperatura", "K", null, 293.15);
        assertEquals("C", conversionResult.getUnit());
        assertEquals(20.0, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Temperatura_CelsiusToCelsius() {
        ConversionResultDTO conversionResult = unitConversionService.convertUnits("Temperatura", "C", null, 37.15);
        assertEquals("C", conversionResult.getUnit());
        assertEquals(37.15, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Temperatura_WrongUnit_ToCelsius() {
        IllegalArgumentException exception = assertThrows(
            IllegalArgumentException.class,
            () -> {
                unitConversionService.convertUnits("Temperatura", "X", null, 43d);
            }
        );
        assertEquals("Temperature unit not supported: X", exception.getMessage());
    }
}
