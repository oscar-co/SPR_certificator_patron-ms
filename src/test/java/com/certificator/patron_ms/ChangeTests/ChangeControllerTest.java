package com.certificator.patron_ms.ChangeTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static com.certificator.patron_ms.utils.TestUtils.readJsonFromFile;



import com.certificator.patron_ms.Controller.ChangeController;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Service.ChangeService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(ChangeController.class)
public class ChangeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ChangeService changeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
    }


    @Test
    void getPatronAvailable_returnsAvailableList() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurements.json");
        Certificate cert = objectMapper.readValue(json, Certificate.class);

        when(changeService.getPatronesByMeasure(any(Change.class))).thenReturn(List.of(cert));

        mockMvc.perform(post("/api/patrones/patrones-disponibles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"magnitud\": \"temperatura\", \"inputUnit\": \"C\", \"inputValue\": 34.3}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(1));
    }

    @Test
    void getUncertaintyByPtn_returnsValue() throws Exception {
        when(changeService.getUncertaintyByPtnS(any(UncertaintyByPtnDTO.class))).thenReturn(0.12);

        mockMvc.perform(post("/api/patrones/incertidumbre-patron")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"inputUnit\": \"C\", \"inputValue\": 34.3, \"nameIdentify\": \"PTN-001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data").value("0.12"));
    }
}
