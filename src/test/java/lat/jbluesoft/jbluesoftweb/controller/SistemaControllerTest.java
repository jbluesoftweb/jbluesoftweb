package lat.jbluesoft.jbluesoftweb.controller;

import java.util.Optional;

import lat.jbluesoft.jbluesoftweb.model.Sistema;
import lat.jbluesoft.jbluesoftweb.service.SistemaService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.webmvc.test.autoconfigure.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.Matchers.containsString;
import static org.hamcrest.Matchers.not;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.view;

@WebMvcTest(SistemaController.class)
class SistemaControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SistemaService sistemaService;

    @Test
    void detalleDeSistemaExistente() throws Exception {
        given(sistemaService.findBySlug("erp-admin")).willReturn(Optional.of(new Sistema(
                "erp-admin", "ERP-ADMIN", "Descripción corta", "Descripción larga",
                "/assets/img/portafolio-ameztravel.webp", null,
                "https://github.com/jbluesoftweb/erp-admin", null)));

        mockMvc.perform(get("/sistemas/erp-admin"))
                .andExpect(status().isOk())
                .andExpect(view().name("sistemas/detalle"))
                .andExpect(content().string(containsString("<title>ERP-ADMIN | JBlueSoft</title>")))
                .andExpect(content().string(containsString("href=\"https://github.com/jbluesoftweb/erp-admin\"")))
                .andExpect(content().string(not(containsString("youtube-nocookie"))));
    }

    @Test
    void detalleDeSistemaInexistenteDevuelve404() throws Exception {
        given(sistemaService.findBySlug("no-existe")).willReturn(Optional.empty());

        mockMvc.perform(get("/sistemas/no-existe"))
                .andExpect(status().isNotFound());
    }

    @Test
    void detalleEscapaHtmlYMuestraYoutube() throws Exception {
        given(sistemaService.findBySlug("demo")).willReturn(Optional.of(new Sistema(
                "demo", "<script>alert(1)</script>", "Corta", "Larga",
                "/assets/img/drivex.webp", "PLabc_123", null, null)));

        mockMvc.perform(get("/sistemas/demo"))
                .andExpect(status().isOk())
                .andExpect(content().string(not(containsString("<script>alert(1)</script>"))))
                .andExpect(content().string(containsString("&lt;script&gt;alert(1)&lt;/script&gt;")))
                .andExpect(content().string(containsString("https://www.youtube-nocookie.com/embed/videoseries?list=PLabc_123")));
    }

}
