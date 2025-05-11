package com.certificator.patron_ms.conversionTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static com.certificator.patron_ms.utils.TestUtils.readJsonFromFile;

import com.certificator.patron_ms.certificate.Certificate;
import com.certificator.patron_ms.config.security.SecurityConfig;
import com.certificator.patron_ms.conversion.ConversionFactorController;
import com.certificator.patron_ms.conversion.ConversionFactorService;
import com.certificator.patron_ms.conversion.dto.ConversionResponseDTO;
import com.certificator.patron_ms.conversion.dto.ConversionRequestDTO;
import com.certificator.patron_ms.conversion.dto.UncertaintyByPtnDTO;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(ConversionFactorController.class)
@Import(SecurityConfig.class)
public class ConversionFactorControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ConversionFactorService changeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getPatronAvailable_returnsAvailableList() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurements.json");
        Certificate cert = objectMapper.readValue(json, Certificate.class);

        when(changeService.getPatronesByMeasure(any(ConversionResponseDTO.class))).thenReturn(List.of(cert));

        mockMvc.perform(post("/api/patrones/patrones-disponibles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"magnitud\": \"temperatura\", \"inputUnit\": \"C\", \"inputValue\": 34.3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getUncertaintyByPtn_returnsValue() throws Exception {
        when(changeService.getUncertaintyByPtnS(any(UncertaintyByPtnDTO.class))).thenReturn(0.12);

        mockMvc.perform(post("/api/patrones/incertidumbre-patron")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"inputUnit\": \"C\", \"inputValue\": 34.3, \"nameIdentify\": \"PTN-001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("0.12"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getChangeByUnitsAndValue_returnsConversionResult() throws Exception {

        ConversionRequestDTO requestDto = new ConversionRequestDTO("presion", "bar", "mbar", 34.0);
        ConversionResponseDTO responseDto = new ConversionResponseDTO("presion", "bar", "mbar", 34.0, 34000.0 );

        when(changeService.convert(any(ConversionRequestDTO.class))).thenReturn(responseDto);

        // Act & Assert: realiza el POST y valida respuesta
        mockMvc.perform(post("/api/patrones/cambio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.status").value("success"))
            .andExpect(jsonPath("$.data.inputValue").value(34.0))
            .andExpect(jsonPath("$.data.outputValue").value(34000.0))
            .andExpect(jsonPath("$.data.inputUnit").value("bar"))
            .andExpect(jsonPath("$.data.outputUnit").value("mbar"))
            .andExpect(jsonPath("$.data.magnitud").value("presion"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void convert_shouldReturnError_whenFactorNotFound() throws Exception {

        ConversionRequestDTO requestDto = new ConversionRequestDTO("presion", "bar", "mbar", 346767.0);

        when(changeService.convert(any(ConversionRequestDTO.class)))
            .thenThrow(new RuntimeException("No se encontró factor de conversión"));

        // Act & Assert: esperamos un 500 del servidor por la excepción lanzada
        mockMvc.perform(post("/api/patrones/cambio")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(requestDto)))
            .andExpect(status().isInternalServerError())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").value("Error en la llamada"))
            .andExpect(jsonPath("$.data").value("No se encontró factor de conversión"));
    }
}
