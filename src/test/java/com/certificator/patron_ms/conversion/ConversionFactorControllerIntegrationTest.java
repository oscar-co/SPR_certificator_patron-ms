package com.certificator.patron_ms.conversion;

import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.conversion.dto.UncertaintyByPtnDTO;
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

import java.io.IOException;
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

    @BeforeAll
    void isert() throws IOException{
        testUtils.insertListOfCertificates();
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
    void testGet_IncorrectMagnitude_ChangeByUnitsAndValue_shouldReturnNotValidResult() throws Exception {
        HttpHeaders headers = testUtils.buildHeaders(userToken);
        String jsonRequest = """
            { "magnitud": "areaxxx", "inputUnit": "cm2", "outputUnit": "m2", "inputValue": 7500 }
         """;
        
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones/cambio", entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiResponse<ConversionResponseDTO> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals("error", responseObj.getStatus());
        assertEquals("Magnitud no reconocida: areaxxx", responseObj.getMessage());
    }
    

    @Test
    void testGet_IncorrectOutputUnit_ChangeByUnitsAndValue_shouldReturnNotValidResult() throws Exception {
        HttpHeaders headers = testUtils.buildHeaders(userToken);
        String jsonRequest = """
            { "magnitud": "area", "inputUnit": "cm2", "outputUnit": "mj2", "inputValue": 7500 }
         """;
        
        HttpEntity<String> entity = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity("/api/patrones/cambio", entity, String.class);
        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());

        ApiResponse<ConversionResponseDTO> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals("error", responseObj.getStatus());
        assertEquals("Area unit not supported: mj2", responseObj.getMessage());
    }

    @ParameterizedTest
    @MethodSource("provideIncertidumbreData")
    void getUncertaintyByPtn_shouldReturnUncertainty(String inputUnit, Double inputValue, String nameIdentify, Double outputValue) throws Exception {

        UncertaintyByPtnDTO dto = new UncertaintyByPtnDTO(inputUnit, inputValue, nameIdentify, outputValue);
        
        HttpHeaders headers = testUtils.buildHeaders(userToken);
        HttpEntity<String> request = new HttpEntity<>( objectMapper.writeValueAsString(dto), headers );

        ResponseEntity<String> response = restTemplate.postForEntity( "/api/patrones/incertidumbre-patron", request, String.class );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<Double> responseObj = objectMapper.readValue( response.getBody(), new TypeReference<>() {} );
        assertEquals("success", responseObj.getStatus());
        assertNotNull(responseObj.getData());
        assertEquals(outputValue, responseObj.getData(), "XXXXX");
    }

    @Test
    void getUncertaintyByPtn_outOfRange_shouldReturnUncertainty() throws Exception {
        
        HttpHeaders headers = testUtils.buildHeaders(userToken);
        String request = """
                { "inputUnit": "C", "inputValue": 160, "nameIdentify": "PTN-002" }
                """;
        HttpEntity<String> entity = new HttpEntity<>( request, headers );
        ResponseEntity<String> response = restTemplate.postForEntity( "/api/patrones/incertidumbre-patron", entity, String.class );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ApiResponse<Double> responseObj = objectMapper.readValue( response.getBody(), new TypeReference<>() {} );
        assertEquals("error", responseObj.getStatus());
        assertNull(responseObj.getData());
        assertEquals("404 NOT_FOUND \"No se encontró incertidumbre para el valor convertido: 160.0\"", responseObj.getMessage());
    }

    @Test
    void getUncertaintyByPtn_wrongPtn_shouldReturnUncertainty() throws Exception {
        
        HttpHeaders headers = testUtils.buildHeaders(userToken);
        String request = """
                { "inputUnit": "C", "inputValue": 34, "nameIdentify": "PTN-007" }
                """;
        HttpEntity<String> entity = new HttpEntity<>( request, headers );
        ResponseEntity<String> response = restTemplate.postForEntity( "/api/patrones/incertidumbre-patron", entity, String.class );
        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());

        ApiResponse<Double> responseObj = objectMapper.readValue( response.getBody(), new TypeReference<>() {} );
        assertEquals("error", responseObj.getStatus());
        assertNull(responseObj.getData());
        assertEquals("404 NOT_FOUND \"No se encontró magnitud para el identificador: PTN-007\"", responseObj.getMessage());
    }


    @Test
    void getUnitsByMagnitud_shouldReturnList() throws Exception {

        String magnitud = "area";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate
            .exchange( "/api/patrones/unidades/" + magnitud, HttpMethod.GET, request, String.class );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<List<String>> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals("success", responseObj.getStatus());
        assertNotNull(responseObj.getData());
        assertFalse(responseObj.getData().isEmpty(), "Debe devolver al menos una unidad para la magnitud");
        assertEquals(responseObj.getData().size(), 6);
        assertTrue(responseObj.getData().contains("ft2"));
    }


    @Test
    void getUnitsByMagnitud_wrongMagnitud_shouldNotReturnResults() throws Exception {

        String magnitud = "rorororo";
        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(userToken);
        HttpEntity<Void> request = new HttpEntity<>(headers);

        ResponseEntity<String> response = restTemplate
            .exchange( "/api/patrones/unidades/" + magnitud, HttpMethod.GET, request, String.class );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<List<String>> responseObj = objectMapper.readValue(response.getBody(), new TypeReference<>() {});
        assertEquals("success", responseObj.getStatus());
        assertNotNull(responseObj.getData());
        assertTrue(responseObj.getData().isEmpty());
        assertEquals(responseObj.getData().size(), 0);
    }


    @Test
    void getPatronAvailable_shouldReturnAvailableList() throws Exception {
        //testUtils.insertListOfCertificates();

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

    @Test
    void getPatronAvailable_should_Not_ReturnAvailableCerts() throws Exception {
        //testUtils.insertListOfCertificates();

        HttpHeaders headers = testUtils.buildHeaders(userToken);

        String jsonRequest = """
            {
                "magnitud": "temperatura",
                "inputUnit": "C",
                "inputValue": 134.3
            }
        """;

        HttpEntity<String> request = new HttpEntity<>(jsonRequest, headers);
        ResponseEntity<String> response = restTemplate.postForEntity( "/api/patrones/patrones-disponibles", request, String.class );
        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<List<Certificate>> responseObj = objectMapper.readValue( response.getBody(), new TypeReference<>() {} );
        assertEquals("success", responseObj.getStatus());
        assertNotNull(responseObj.getData());
        assertTrue(responseObj.getData().isEmpty(), "Debe devolver al menos un patrón disponible");
        assertEquals(responseObj.getData().size(), 0);
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

    // Método fuente para los datos de prueba
    static Stream<Arguments> provideIncertidumbreData() {
        return Stream.of(

            Arguments.of("C", 50.0, "PTN-001", 0.15),
            Arguments.of("C", 80.0, "PTN-002", 0.12)  
        );
    }
}
