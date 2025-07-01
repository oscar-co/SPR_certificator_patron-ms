package com.certificator.patron_ms.conversion;

import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.certificate.CertificateRepository;
import com.certificator.patron_ms.shared.dto.ApiResponse;
import com.certificator.patron_ms.user.UserRepository;
import com.certificator.patron_ms.utils.TestUtils;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.junit.jupiter.api.*;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
@ActiveProfiles("test")
public class ConversionFactorControllerIntegrationTest {

    @Autowired private TestRestTemplate restTemplate;
    @Autowired private ObjectMapper objectMapper;
    @Autowired private UserRepository userRepository;
    @Autowired private TestUtils testUtils;
    @Autowired private CertificateRepository certificateRepository;

    private String userToken;

    @BeforeEach
    void setup() {
        userToken = testUtils.registerAndLogin("userConv", "user123", "user@conv.com", "ROLE_USER");
    }

    @AfterEach
    void cleanup() {
        userRepository.findByUsername("userConv").ifPresent(userRepository::delete);
    }

    @AfterAll
    void cleanupAll() {
        certificateRepository.deleteAll();   
    }

    // Test parametrizado con los datos proporcionados por provideConversionData()
    @ParameterizedTest
    @MethodSource("provideConversionData") //El metodo de carga con diferentes sets de datos esta al final de la clase
    void testGetChangeByUnitsAndValue_shouldReturnValidResult(String magnitud, String inputUnit, String outputUnit, double inputValue, double expectedOutputValue) throws Exception {
        // Crear el DTO de solicitud con los parámetros proporcionados
        ConversionRequestDTO request = new ConversionRequestDTO(magnitud, inputUnit, outputUnit, inputValue);

        HttpHeaders headers = testUtils.buildHeaders(userToken);
        HttpEntity<String> entity = new HttpEntity<>(objectMapper.writeValueAsString(request), headers);

        ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones/cambio", entity, String.class);
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<ConversionResponseDTO> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<>() {});

        assertEquals("success", responseObj.getStatus());
        assertEquals(inputValue, responseObj.getData().getInputValue());
        assertEquals(expectedOutputValue, responseObj.getData().getOutputValue());
        assertEquals(inputUnit, responseObj.getData().getInputUnit());
        assertEquals(outputUnit, responseObj.getData().getOutputUnit());
        assertEquals(magnitud, responseObj.getData().getMagnitud());
    }

    @Test
    void getPatronAvailable_shouldReturnAvailableList() throws Exception {
        testUtils.insertListOfCertificates();

        HttpHeaders headers = testUtils.buildHeaders(userToken);

        String jsonRequest = """
        {
                "magnitud": "temperatura",
                "inputUnit": "C",
                "inputValue": 34.3
            }
        """;

        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( "/api/patrones/patrones-disponibles", request, String.class );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<List<Certificate>> responseObj = objectMapper.readValue( response.getBody(), new TypeReference<>() {} );
        assertEquals("success", responseObj.getStatus());
        assertNotNull(responseObj.getData());
        assertFalse(responseObj.getData().isEmpty(), "Debe devolver al menos un patrón disponible");
        assertEquals(responseObj.getData().size(), 2);
    }
    // Método fuente para los datos de prueba
    static Stream<Arguments> provideConversionData() {
        return Stream.of(
            // Presión
            Arguments.of("presion", "Pa", "bar", 100000.0, 1.0),  
            Arguments.of("presion", "bar", "Pa", 1.0, 100000.0),  
            Arguments.of("presion", "Pa", "psi", 6894.76, 1.00000220088),  
            // Temperatura
            Arguments.of("temperatura", "C", "F", 100.0, 212.0),  
            Arguments.of("temperatura", "K", "C", 273.15, 0.0),  
            // Masa
            Arguments.of("masa", "kg", "g", 1.0, 1000.0),  
            Arguments.of("masa", "g", "mg", 1.0, 1000.0),  
            // Longitud
            Arguments.of("longitud", "m", "km", 1000.0, 1.0),  
            Arguments.of("longitud", "km", "mi", 1.0, 0.621371),  
            // Área
            Arguments.of("area", "m2", "km2", 7500.0, 0.0075),
            Arguments.of("area", "cm2", "m2", 500.0, 0.05)  
        );
    }
}
