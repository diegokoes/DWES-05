package es.daw.cookies.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.ResponseCookie;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@Controller
public class CookieController {

//    @PostMapping("/setCookie")
//    public String setCookie(@RequestParam("genre") String preferencia, HttpServletResponse response, Model model) {
//
//        Cookie cookie = new Cookie("genre", preferencia);
//        cookie.setMaxAge(7 * 24 * 60 * 60); // La cookie durará 7 días
//        response.addCookie(cookie);
//
//        model.addAttribute("preferencia",preferencia);
//
//        return "formulario";
//    }

    @PostMapping("/setCookie")
    public String setCookie(@RequestParam("genre") String preferencia, Model model, HttpServletResponse response) {
        ResponseCookie cookie = ResponseCookie.from("genre", preferencia)
                .httpOnly(true) // La cookie solo puede ser leída por el servidor
                //.secure(true) // Solo se envía a través de HTTPS (opcional, pero recomendado en producción)
                .path("/") // Accesible en toda la aplicación
                .maxAge(7 * 24 * 60 * 60) // La cookie expira en 7 días
                .build();

        response.addHeader("Set-Cookie", cookie.toString());

        model.addAttribute("preferencia",preferencia);

        return "formulario";
    }


//    @GetMapping("/ver-form")
//    public String verForm(Model model, HttpServletRequest request) {
//        Cookie[] cookies = request.getCookies();
//
//        if (cookies != null) {
//            // Leer la cookie por si ya ha elegido la preferencia
//            Optional<String> cookie = Arrays.stream(cookies)
//                    .filter(c -> "genre".equals(c.getName()))
//                    .map(Cookie::getValue)
//                    .findFirst();
//
//            if (cookie.isPresent()) {
//                model.addAttribute("preferencia", cookie.get());
//            }
//        }
//
//        return "formulario";
//    }
    @GetMapping("/ver-form")
    public String verForm(Model model, @CookieValue(value = "genre", defaultValue = "") String genre) {
        // Si la cookie "genre" está presente, agregamos el valor al modelo
        if (!genre.isEmpty()) {
            model.addAttribute("preferencia", genre);
        }

        return "formulario";
    }

}
