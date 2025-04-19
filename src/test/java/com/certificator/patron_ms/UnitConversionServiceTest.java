package com.certificator.patron_ms;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

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
}
