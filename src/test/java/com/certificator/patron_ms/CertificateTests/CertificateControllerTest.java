package com.certificator.patron_ms.CertificateTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static com.certificator.patron_ms.utils.TestUtils.readJsonFromFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import com.certificator.patron_ms.Controller.CertificateController;
import com.certificator.patron_ms.DTO.UncertaintyByPtnDTO;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Model.Change;
import com.certificator.patron_ms.Service.CertificateService;
import com.certificator.patron_ms.Service.ChangeService;
import com.certificator.patron_ms.utils.TestUtils;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(CertificateController.class)
public class CertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CertificateService certificateService;

    // @MockBean
    // private ChangeService changeService;

    @Autowired
    private ObjectMapper objectMapper;

    @BeforeEach
    void setup() {
        objectMapper.registerModule(new JavaTimeModule());
    }

    @Test
    void createPtn_returnsCreatedCertificate() throws Exception {
        Certificate cert = new Certificate();
        cert.setNameIdentify("PTN-001");

        when(certificateService.createNewPtn(any(Certificate.class))).thenReturn(cert);

        mockMvc.perform(post("/api/patrones")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nameIdentify\": \"PTN-001\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameIdentify").value("PTN-001"));
    }

    @Test
    void getPtnById_returnsCertificate() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurements.json");

        Certificate cert = objectMapper.readValue(json, Certificate.class);
        when(certificateService.getPtnById(1L)).thenReturn(cert);

        mockMvc.perform(get("/api/patrones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameIdentify").value("PTN-001"));
    }

    @Test
    void getAllCertificates_returnsList() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurementsVarios.json");

        List<Certificate> certs = Arrays.asList(objectMapper.readValue(json, Certificate[].class));

        when(certificateService.getAllCertificates()).thenReturn(certs);

        mockMvc.perform(get("/api/patrones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.length()").value(3))
                .andExpect(jsonPath("$[0].measurements[0].reference").value(100.0));
    }

    @Test
    void deleteCertificate_returnsSuccessMessage() throws Exception {
        doNothing().when(certificateService).deleteCertificate(1L);

        mockMvc.perform(delete("/api/patrones/1"))
                .andExpect(status().isOk())
                .andExpect(content().string("Certificado eliminado correctamente"));
    }

    @Test
    void updateCertificate_returnsUpdatedCert() throws Exception {
        Certificate cert = new Certificate();
        cert.setNameIdentify("PTN-UPDATED");

        when(certificateService.updateCertificateById(eq(1L), any(Certificate.class))).thenReturn(cert);

        mockMvc.perform(put("/api/patrones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"nameIdentify\": \"PTN-UPDATED\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nameIdentify").value("PTN-UPDATED"));
    }
}
