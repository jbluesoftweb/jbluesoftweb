package lat.jbluesoft.jbluesoftweb.controller;

import java.time.Year;
import java.util.List;
import java.util.regex.Pattern;

import lat.jbluesoft.jbluesoftweb.model.Sistema;
import lat.jbluesoft.jbluesoftweb.service.SistemaService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(HomeController.class)
class HomeControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SistemaService sistemaService;

    @BeforeEach
    void setUp() {
        given(sistemaService.findAll()).willReturn(List.of(new Sistema(
                "erp-admin", "ERP-ADMIN", "Descripción corta", "Descripción larga",
                "/assets/img/portafolio-ameztravel.webp", null,
                "https://github.com/jbluesoftweb/erp-admin", null)));
    }

    @Test
    void homeRendersIndexView() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(view().name("index"))
                .andExpect(content().string(containsString("JBlueSoft")));
    }

    @Test
    void homeRendersLayoutFragments() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("<title>JBlueSoft | Inicio</title>")))
                .andExpect(content().string(containsString("© JBLUESOFT 2024 - " + Year.now().getValue())))
                .andExpect(content().string(containsString("integrity=\"sha384-")));
    }

    @Test
    void homeReferenciaRecursosVersionadosPorContenido() throws Exception {
        String html = mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andReturn().getResponse().getContentAsString();

        assertTrue(Pattern.compile("href=\"/css/styles-[0-9a-f]+\\.css\"").matcher(html).find(),
                "styles.css debe referenciarse con hash de contenido");
        assertTrue(Pattern.compile("src=\"/js/main-[0-9a-f]+\\.js\"").matcher(html).find(),
                "main.js debe referenciarse con hash de contenido");
    }

    @Test
    void homeEnlazaAlDetalleDeCadaSistema() throws Exception {
        mockMvc.perform(get("/"))
                .andExpect(status().isOk())
                .andExpect(content().string(containsString("href=\"/sistemas/erp-admin\"")))
                .andExpect(content().string(containsString("ERP-ADMIN")));
    }

}
