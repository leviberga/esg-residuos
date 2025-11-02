package br.com.fiap.esg_residuos.integration;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.httpBasic;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
@ActiveProfiles("test")
class ApiIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void createPontoAndList_itWorks() throws Exception {
        String json = "{\"nome\":\"Integration Ponto\",\"endereco\":\"R Teste\",\"cidade\":\"CidadeTest\",\"tipo\":\"Papel\",\"volumeMaximo\":100.0}";

        mockMvc.perform(post("/api/ponto-coleta")
                        .with(httpBasic("admin", "admin"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isCreated());

        mockMvc.perform(get("/api/pontos-coleta?cidade=CidadeTest")
                        .with(httpBasic("user", "user")))
                .andExpect(status().isOk());
    }

    @Test
    void createPontoWithUserShouldReturnForbidden() throws Exception {
        String json = "{\"nome\":\"Integration Ponto 2\",\"endereco\":\"R Teste\",\"cidade\":\"C2\",\"tipo\":\"Vidro\",\"volumeMaximo\":50.0}";

        mockMvc.perform(post("/api/ponto-coleta")
                        .with(httpBasic("user", "user"))
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(json))
                .andExpect(status().isForbidden());
    }
}
