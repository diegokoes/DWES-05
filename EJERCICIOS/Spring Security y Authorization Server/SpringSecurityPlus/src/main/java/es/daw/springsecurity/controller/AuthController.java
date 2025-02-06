package es.daw.springsecurity.controller;

import es.daw.springsecurity.dto.AuthRequest;
import es.daw.springsecurity.dto.AuthResponse;
import es.daw.springsecurity.dto.RegisterRequest;
import es.daw.springsecurity.entity.Role;
import es.daw.springsecurity.entity.User;
import es.daw.springsecurity.repository.RoleRepository;
import es.daw.springsecurity.repository.UserRepository;
import es.daw.springsecurity.security.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Collections;
import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final RoleRepository roleRepository;
    private final PasswordEncoder passwordEncoder;

    /*
        POST /auth/login

        Recibe un usuario y contraseña.
        Autentica al usuario.
        Genera un JWT y lo devuelve en la respuesta.
     */
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody AuthRequest request) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(request.getUsername(), request.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String token = jwtService.generateToken((UserDetails) authentication.getPrincipal());

        return ResponseEntity.ok(new AuthResponse(token));
    }
    /*
        POST /auth/register

        Recibe un usuario y contraseña.
        Registra un nuevo usuario en la base de datos.
        Devuelve un mensaje de éxito.
     */

    @PostMapping("/register")
    public ResponseEntity<String> register(@RequestBody RegisterRequest request) {

        if (userRepository.findByUsername(request.getUsername()).isPresent()) {
            return ResponseEntity.badRequest().body("El usuario ya existe");
        }

        // Inicializamos roles
        Set<Role> roles = new HashSet<>();

        // Asegurar que el rol enviado tenga el prefijo ROLE_
        String roleName = request.getRole() != null ? request.getRole().toUpperCase() : "USER";
        if (!roleName.startsWith("ROLE_")) {
            roleName = "ROLE_" + roleName;
        }

        // Buscamos el rol en la base de datos
        Optional<Role> roleOptional = roleRepository.findByName(roleName);
        if (roleOptional.isPresent()) {
            roles.add(roleOptional.get());
        } else {
            return ResponseEntity.badRequest().body("Rol inválido. Usa 'ADMIN' o 'USER'.");
        }

        // Creamos el nuevo usuario
        User newUser = new User();
        newUser.setUsername(request.getUsername());
        newUser.setPassword(passwordEncoder.encode(request.getPassword()));
        newUser.setRoles(roles);

        userRepository.save(newUser);

        return ResponseEntity.ok("Usuario registrado con éxito con rol: " + roleName);
    }

}

