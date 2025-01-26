package es.daw.cookies.controller;


import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Optional;

@RestController
@RequestMapping("/cookies")
public class CookieRestController {


    @GetMapping("/read-cookie")
    public String readCookie(@CookieValue(name = "user-id", defaultValue = "defaultUser") String userId) {
        return "Cookie Value: " + userId;
    }

    @GetMapping("/set-cookie")
    public ResponseEntity<String> setCookie(@RequestParam String userId) {
        ResponseCookie cookie = ResponseCookie.from("user-id", userId)
                .httpOnly(true)
                .secure(true)
                .path("/")
                .maxAge(7 * 24 * 60 * 60)
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Cookie creada con user-id: " + userId);
    }


    @GetMapping("/delete-cookie")
    public ResponseEntity<String> deleteCookie() {
        ResponseCookie cookie = ResponseCookie.from("user-id", "")
                .maxAge(0) // Expira inmediatamente
                .path("/")
                .build();

        return ResponseEntity.ok()
                .header(HttpHeaders.SET_COOKIE, cookie.toString())
                .body("Cookie eliminada");
    }


    // --------------------------------

    @PostMapping("/setCookieNativa")
    @ResponseBody
    public String setCookieNativa(@RequestParam("name") String name,@RequestParam("value") String value, HttpServletResponse response) {
        Cookie cookie = new Cookie(name, value);
        cookie.setMaxAge(7 * 24 * 60 * 60); // La cookie durará 7 días
        response.addCookie(cookie);
        return "Preferencia de género guardada exitosamente!";
    }



    @GetMapping("/getCookieNativa")
    public String getCookieNativa(@RequestParam("name") String name,HttpServletRequest request) {
        Optional<String> cookie = Arrays.stream(request.getCookies())
                .filter(c -> name.equals(c.getName()))
                .map(Cookie::getValue)
                .findFirst();

        if (cookie.isPresent()) {
            return cookie.get();
        }
        return "Cookie no encontrada";
    }

    // --------------------------------
}
