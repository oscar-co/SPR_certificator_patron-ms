package com.certificator.patron_ms.ChangeTests;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.certificator.patron_ms.Model.ConversionResult;
import com.certificator.patron_ms.Service.UnitConversionService;


@SpringBootTest
public class UnitConversionServiceTest {

    @Autowired
    private UnitConversionService unitConversionService;


    @Test
    void testConvert_Presion_Pa_to_mbar() {

        Double result = unitConversionService.calculoMagnitudes("Pa", "mbar", 23);
        assertEquals(0.23, result);
    }

    @Test
    void testConvert_Presion_mbar_to_Pa() {

        Double result = unitConversionService.calculoMagnitudes("mbar", "Pa", 0.05);
        assertEquals(5.0, result);
    }

    @Test
    void testConvert_Presion_bar_to_Pa() {
        Double result = unitConversionService.calculoMagnitudes("bar", "Pa", 1.0);
        assertEquals(100000.0, result);
    }

    @Test
    void testConvert_Presion_Pa_to_bar() {
        Double result = unitConversionService.calculoMagnitudes("Pa", "bar", 500000.0);
        assertEquals(5.0, result);
    }

    @Test
    void testConvert_Presion_atm_to_Pa() {
        Double result = unitConversionService.calculoMagnitudes("atm", "Pa", 1.0);
        assertEquals(101325.0, result);
    }

    @Test
    void testConvert_Presion_mmHg_to_Pa() {
        Double result = unitConversionService.calculoMagnitudes("mmHg", "Pa", 7.0);
        assertEquals(933.254, result);
    }

    @Test
    void testConvert_Presion_WrongUnit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unitConversionService.calculoMagnitudes("Km", "Pa", 1.0);
        });

        assertEquals("Factor no encontrado para Km → Pa", exception.getMessage());
    }

    @Test
    void testConvert_temperatura_FtoC(){
        Double result = UnitConversionService.calculoTemperatura("F", 69.8);
        assertEquals(21, result);
    }

    @Test
    void testConvert_temperatura_KtoC(){
        Double result = UnitConversionService.calculoTemperatura("K", 300.0);
        assertEquals(26.850000000000023, result);
    }

    @Test
    void testConvert_temperatura_CtoC(){
        Double result = UnitConversionService.calculoTemperatura("C", 38.0);
        assertEquals(38.0, result);
    }

    @Test
    void testConvert_Temperatura_WrongUnit() {
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            UnitConversionService.calculoTemperatura("Km", 10.0);
        });
        assertEquals("Unsupported temperature conversion from Km to C", exception.getMessage());
    }

    @Test
    void testConvertToReferenceUnit_Presion_mmHg_to_bar() {
        ConversionResult conversionResult = unitConversionService.convertToReferenceUnit("presion", "psi", 160.0);
        assertEquals(11.031616, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Presion_WrongUnit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unitConversionService.convertToReferenceUnit("presion", "Km", 10.0);
        });
        assertEquals("Pressure unit not supported: Km", exception.getMessage());
    }

    // TODO Se queda pendiente hasta insertar los facotrees de cambio de masa al data.sql
    // @Test
    // void testConvertToReferenceUnit_Masa_mg_to_g() {
    //     ConversionResult conversionResult = unitConversionService.convertToReferenceUnit("masa", "mg", 3400.0);
    //     assertEquals(11.031616, conversionResult.getConvertedValue());
    // }

    // @Test
    // void testConvertToReferenceUnit_Masa_WrongUnit() {
    //     Exception exception = assertThrows(IllegalArgumentException.class, () -> {
    //         unitConversionService.convertToReferenceUnit("masa", "Km", 10.0);
    //     });
    //     assertEquals("Mass unit not supported: Km", exception.getMessage());
    // }

}
