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
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.web.client.TestRestTemplate;
import org.springframework.http.*;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

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

    @Test
    void testGetChangeByUnitsAndValue_shouldReturnValidResult() throws Exception {
        ConversionRequestDTO request = new ConversionRequestDTO("presion", "bar", "mbar", 2.0);

        HttpHeaders headers = testUtils.buildHeaders(userToken);

        HttpEntity<String> entity = new HttpEntity<>(
            objectMapper.writeValueAsString(request), headers);

        ResponseEntity<String> response = restTemplate.postForEntity(
            "/api/patrones/cambio", entity, String.class);

        assertEquals(HttpStatus.OK, response.getStatusCode());

        ApiResponse<ConversionResponseDTO> responseObj = objectMapper.readValue(
            response.getBody(), new TypeReference<>() {}
        );

        assertEquals("success", responseObj.getStatus());
        assertEquals(2.0, responseObj.getData().getInputValue());
        assertEquals(2000.0, responseObj.getData().getOutputValue());
        assertEquals("bar", responseObj.getData().getInputUnit());
        assertEquals("mbar", responseObj.getData().getOutputUnit());
        assertEquals("presion", responseObj.getData().getMagnitud());
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
}
