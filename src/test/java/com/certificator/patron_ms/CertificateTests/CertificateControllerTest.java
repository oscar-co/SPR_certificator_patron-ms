package com.certificator.patron_ms.CertificateTests;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Arrays;
import java.util.List;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

import static com.certificator.patron_ms.utils.TestUtils.readJsonFromFile;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.certificator.patron_ms.Config.security.SecurityConfig;
import com.certificator.patron_ms.Controller.CertificateController;
import com.certificator.patron_ms.Model.Certificate;
import com.certificator.patron_ms.Service.CertificateService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;

@WebMvcTest(CertificateController.class)
@Import(SecurityConfig.class)
public class CertificateControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private CertificateService certificateService;

    @Autowired
    private ObjectMapper objectMapper;

    String json;

    @BeforeEach
    void setup() throws Exception {
        objectMapper.registerModule(new JavaTimeModule());
        json = readJsonFromFile("test-data/certificate-with-measurements.json");
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createPtn_returnsCreatedCertificate() throws Exception {
        
        Certificate cert = objectMapper.readValue(json, Certificate.class);
        when(certificateService.createNewPtn(any(Certificate.class))).thenReturn(cert);

        mockMvc.perform(post("/api/patrones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isOk())
            .andExpect(jsonPath("$.data.nameIdentify").value("PTN-001")
        );
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void createPtn_withUser_NoPermision_returnsCreatedCertificate() throws Exception {
        
        Certificate cert = objectMapper.readValue(json, Certificate.class);
        when(certificateService.createNewPtn(any(Certificate.class))).thenReturn(cert);

        mockMvc.perform(post("/api/patrones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
            .andExpect(status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void createCertificate_withMissingFields_shouldReturnBadRequest() throws Exception {
        String invalidJson = "{}";

        mockMvc.perform(post("/api/patrones")
                .contentType(MediaType.APPLICATION_JSON)
                .content(invalidJson))
            .andExpect(status().isBadRequest())
            .andExpect(jsonPath("$.status").value("error"))
            .andExpect(jsonPath("$.message").exists());
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getPtnById_returnsCertificate() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurements.json");
        Certificate cert = objectMapper.readValue(json, Certificate.class);
        when(certificateService.getPtnById(1L)).thenReturn(cert);

        mockMvc.perform(get("/api/patrones/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nameIdentify").value("PTN-001"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void getPtnById_User_returnsCertificate() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurements.json");
        Certificate cert = objectMapper.readValue(json, Certificate.class);
        when(certificateService.getPtnById(1L)).thenReturn(cert);

        mockMvc.perform(get("/api/patrones/1"))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$.data.nameIdentify").value("PTN-001"));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void getAllCertificates_returnsList() throws Exception {

        String json = readJsonFromFile("test-data/certificate-with-measurementsVarios.json");

        List<Certificate> certs = Arrays.asList(objectMapper.readValue(json, Certificate[].class));

        when(certificateService.getAllCertificates()).thenReturn(certs);

        mockMvc.perform(get("/api/patrones"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.length()").value(3))
                .andExpect(jsonPath("$.data.[0].measurements[0].reference").value(100.0));
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void deleteCertificate_returnsSuccessMessage() throws Exception {
        doNothing().when(certificateService).deleteCertificate(1L);

        mockMvc.perform(delete("/api/patrones/1"))
        .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.message").value("Certificado eliminado correctamente"));
    }

    @Test
    @WithMockUser(username = "user", roles = {"USER"})
    void deleteCertificate_asUser_shouldReturnForbidden() throws Exception {
        doNothing().when(certificateService).deleteCertificate(1L);

        mockMvc.perform(delete("/api/patrones/1"))
        .andDo(print())
                .andExpect(status().isForbidden()
        );
    }

    @Test
    @WithMockUser(username = "admin", roles = {"ADMIN"})
    void updateCertificate_returnsUpdatedCert() throws Exception {
        String json = readJsonFromFile("test-data/certificate-with-measurements.json");
        Certificate cert = objectMapper.readValue(json, Certificate.class);
        cert.setNameIdentify("PTN-UPDATED");

        when(certificateService.updateCertificateById(eq(1L), any(Certificate.class))).thenReturn(cert);

        mockMvc.perform(put("/api/patrones/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.data.nameIdentify").value("PTN-UPDATED"));
    }
}
