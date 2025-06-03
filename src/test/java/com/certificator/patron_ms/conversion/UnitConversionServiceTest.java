package com.certificator.patron_ms.conversion;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.certificator.patron_ms.certificate.CertificateRepository;
import com.certificator.patron_ms.conversion.UnitConversionService;
import com.certificator.patron_ms.conversion.dto.ConversionResultDTO;

// Usamos @TestInstance(TestInstance.Lifecycle.PER_CLASS) para permitir que los métodos @AfterAll y @BeforeAll
// sean no estáticos (non-static). Esto es útil cuando necesitamos acceder a propiedades de instancia,
// como beans inyectados con @Autowired, dentro de esos métodos.
// Por defecto, JUnit 5 requiere que @AfterAll/@BeforeAll sean static, pero este ajuste cambia el ciclo de vida
// para que JUnit reutilice la misma instancia de clase en todos los tests.
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@SpringBootTest
public class UnitConversionServiceTest {

    @Autowired
    private UnitConversionService unitConversionService;

    @Autowired
    private CertificateRepository certificateRepository;

    @AfterAll
    void cleanup() {
        certificateRepository.deleteAll();
    }

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
        Double result = UnitConversionService.calculoTemperatura("F", "C", 69.8);
        assertEquals(21, result);
    }

    @Test
    void testConvert_temperatura_KtoC(){
        Double result = UnitConversionService.calculoTemperatura("K", "C", 300.0);
        assertEquals(26.850000000000023, result);
    }

    @Test
    void testConvert_temperatura_CtoC(){
        Double result = UnitConversionService.calculoTemperatura("C", "C", 38.0);
        assertEquals(38.0, result);
    }

    @Test
    void testConvert_Temperatura_WrongUnit() {
        Exception exception = assertThrows(UnsupportedOperationException.class, () -> {
            UnitConversionService.calculoTemperatura("Km", "C", 10.0);
        });
        assertEquals("Unsupported temperature conversion from: KM to C", exception.getMessage());
    }

    @Test
    void testConvertToReferenceUnit_Presion_mmHg_to_bar() {
        ConversionResultDTO conversionResult = unitConversionService.convertUnits("presion", "psi", null, 160.0);
        assertEquals(11.031616, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Presion_WrongUnit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unitConversionService.convertUnits("presion", "Km", null, 10.0);
        });
        assertEquals("Pressure unit not supported: Km", exception.getMessage());
    }

    @Test
    void testConvertToReferenceUnit_Masa_mg_to_g() {
        ConversionResultDTO conversionResult = unitConversionService.convertUnits("masa", "mg", "g", 3400.0);
        assertEquals(3.4, conversionResult.getConvertedValue());
    }

    @Test
    void testConvertToReferenceUnit_Masa_WrongUnit() {
        Exception exception = assertThrows(IllegalArgumentException.class, () -> {
            unitConversionService.convertUnits("masa", "Km", "g", 10.0);
        });
        assertEquals("Mass unit not supported: Km", exception.getMessage());
    }
}
