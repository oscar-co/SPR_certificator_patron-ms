package com.certificator.patron_ms;

import java.time.LocalDate;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.certificator.patron_ms.Model.Certificate;
import static com.certificator.patron_ms.utils.TestUtils.readJsonFromFile;
import com.fasterxml.jackson.databind.ObjectMapper;

@SpringBootTest
@AutoConfigureMockMvc
public class PatronMsApplicationTests {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    // @Autowired
    // private CertificateRepository certificateRepository;

    @Test
	public void shouldCreateCertificateSuccessfully() throws Exception {
		// Crear un nuevo certificado con los nuevos campos
		Certificate cert = new Certificate();
		cert.setCertificateNumber("12345ABC");
		cert.setInsType("Instrument Type 1");
		cert.setBrand("Brand X");
		cert.setModel("Model A");
		cert.setNameIdentify("Instrument 1");
		cert.setDescription("Description of the first instrument");
		cert.setIssueDate(LocalDate.of(2023, 4, 10));

		// Realizar la petición POST
		mockMvc.perform(post("/api/patrones")  // Asegúrate de que esta ruta coincide con la del controlador
						.contentType(MediaType.APPLICATION_JSON)
						.content(objectMapper.writeValueAsString(cert)))
				.andExpect(status().isOk()) // Espera un código 200 OK
				.andExpect(jsonPath("$.id").exists()) // Verifica que el ID ha sido generado
				.andExpect(jsonPath("$.certificateNumber").value("12345ABC")) // Verifica el número de certificado
				.andExpect(jsonPath("$.insType").value("Instrument Type 1")) // Verifica el tipo de instrumento
				.andExpect(jsonPath("$.brand").value("Brand X")) // Verifica la marca
				.andExpect(jsonPath("$.model").value("Model A")) // Verifica el modelo
				.andExpect(jsonPath("$.nameIdentify").value("Instrument 1")) // Verifica el nombre identificativo
				.andExpect(jsonPath("$.description").value("Description of the first instrument")) // Verifica la descripción
				.andExpect(jsonPath("$.issueDate").value("2023-04-10")); // Verifica la fecha de emisión
	}

	@Test
    public void shouldCreateCertificateWithMeasurementsSuccessfully() throws Exception {
        String json = readJsonFromFile("test-data/certificate-with-measurements.json");

        mockMvc.perform(post("/api/patrones")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").exists())
                .andExpect(jsonPath("$.certificateNumber").value("CERT-001"))
                .andExpect(jsonPath("$.measurements").isArray())
                .andExpect(jsonPath("$.measurements.length()").value(3))
                .andExpect(jsonPath("$.measurements[0].reference").value(100.0));
    }


}