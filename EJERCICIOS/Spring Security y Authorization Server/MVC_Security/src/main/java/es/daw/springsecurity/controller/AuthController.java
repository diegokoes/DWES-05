package es.daw.springsecurity.controller;


import es.daw.springsecurity.entity.Role;
import es.daw.springsecurity.entity.User;
import es.daw.springsecurity.repository.RoleRepository;
import es.daw.springsecurity.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.HashSet;
import java.util.Set;

@Controller
@RequiredArgsConstructor
public class AuthController {

    private final UserService userService;

    private final RoleRepository roleRepository;

    @GetMapping("/register")
    public String mostrarFormRegistro(Model model){

        // Cargar los roles de bd en la página de registro...
        Set<Role> roles = new HashSet<>(roleRepository.findAll());

        // Pendiente... si no encuentra roles.... qué hago???

//        System.out.println("Roles encontrados:");
//        roles.forEach(System.out::println);

        model.addAttribute("roles", roles);
        model.addAttribute("user", new User());  // realmente hace falta???

        return "register";

    }

    @PostMapping("/register")
    //public String registrarUsuario(@ModelAttribute User user, Model model){
    public String registrarUsuario(@ModelAttribute User user){
        System.out.println("******************* USUARIO A REGISTRAR *****************");
        System.out.println(user);
        System.out.println("*********************************************************");
        userService.registerUser(user);

        return "redirect:/login";
    }

    // ------------------------------------ ENDPOINTS DE LOGIN ------------------------------


//    @GetMapping("/login")
//    public String mostrarLogin(){
//        return "login";
//    }

    @GetMapping("/login")
    public String mostrarLogin(@RequestParam(value = "error", required = false) String error, Model model) {
        if (error != null) {
            model.addAttribute("error", "Usuario o contraseña incorrectos. Inténtalo de nuevo.");
        }
        return "login";
    }

    // No se necesita un endpoint para redigirgir a la plantilla home.
    // Está así configurado en Spring Security  (MvcConfig)
//    @GetMapping("/home")
//    public String mostrarHome(Model model) {
//        return "home";
//    }


}

