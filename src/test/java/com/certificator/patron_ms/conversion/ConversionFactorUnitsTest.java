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
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class ConversionFactorUnitsTest {

    @Autowired
    private UnitConversionService unitConversionService;

    @Autowired
    private CertificateRepository certificateRepository;

    @AfterAll
    void cleanup() {
        certificateRepository.deleteAll();
    }

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
