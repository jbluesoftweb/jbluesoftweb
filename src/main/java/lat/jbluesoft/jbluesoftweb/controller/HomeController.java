package lat.jbluesoft.jbluesoftweb.controller;

import lat.jbluesoft.jbluesoftweb.service.SistemaService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class HomeController {

    private final SistemaService sistemaService;

    public HomeController(SistemaService sistemaService) {
        this.sistemaService = sistemaService;
    }

    @GetMapping("/")
    public String start(Model model){
        model.addAttribute("sistemas", sistemaService.findAll());
        return "index";
    }

}
