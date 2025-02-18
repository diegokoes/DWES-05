package es.daw.springsecurity.controller;

import es.daw.springsecurity.model.Evento;
import es.daw.springsecurity.model.EventoResponse;
import es.daw.springsecurity.service.AuthService;
import es.daw.springsecurity.service.EventoService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@Controller
@RequestMapping("/eventos")
@RequiredArgsConstructor
public class EventoController {

    private final EventoService eventoService;
    private final AuthService authService;

    @GetMapping
    public String listarEventos(@RequestParam(defaultValue = "0") int page,
                                @RequestParam(defaultValue = "5") int size,
                                Model model) {
        EventoResponse response = eventoService.obtenerTodosLosEventos(page, size);
        model.addAttribute("eventos", response.getContent());
        model.addAttribute("paginaActual", page);
        model.addAttribute("tamanio", size);
        model.addAttribute("totalPaginas", response.getTotalPages());
        return "eventos/lista";
    }

    @GetMapping("/nuevo")
    public String mostrarFormulario(Model model) {
        model.addAttribute("evento", new Evento());
        return "eventos/nuevo";
    }

    @PostMapping("/guardar")
    public String guardarEvento(@Valid @ModelAttribute Evento evento, BindingResult result, Model model) {

        System.out.println("*********************** GUARDAR EVENTO **************");
        if (result.hasErrors()) {
            System.out.println(result.getAllErrors());
            return "eventos/nuevo";
        }

        String token = authService.obtenerToken(); // Autenticación contra el API REST

        System.out.println("Token: " + token);

        // Campos fijos...
        evento.setFechaPublicacion(LocalDateTime.now());
        evento.setOrganizadorCodigo("ORG001");

        System.out.println("Evento: " + evento);

        eventoService.guardarEvento(evento, token); // Uso del token para la petición

        return "redirect:/eventos";
    }


}
