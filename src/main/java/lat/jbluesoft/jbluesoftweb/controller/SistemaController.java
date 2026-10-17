package lat.jbluesoft.jbluesoftweb.controller;

import lat.jbluesoft.jbluesoftweb.model.Sistema;
import lat.jbluesoft.jbluesoftweb.service.SistemaService;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.server.ResponseStatusException;

@Controller
public class SistemaController {

    private final SistemaService sistemaService;

    public SistemaController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @GetMapping("/sistemas/{slug}")
    public String detalle(@PathVariable String slug, Model model) {
        Sistema sistema = sistemaService.findBySlug(slug)
                .orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
        model.addAttribute("sistema", sistema);
        return "sistemas/detalle";
    }

}
