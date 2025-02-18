package es.daw.api.eventos.controller;

import es.daw.api.eventos.dto.AuthRequest;
import es.daw.api.eventos.dto.AuthResponse;
import es.daw.api.eventos.dto.RegisterRequest;
import es.daw.api.eventos.entity.Rol;
import es.daw.api.eventos.entity.Usuario;
import es.daw.api.eventos.repository.RolRepository;
import es.daw.api.eventos.repository.UsuarioRepository;
import es.daw.api.eventos.security.JwtService;
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

import java.util.HashSet;
import java.util.Optional;
import java.util.Set;

@RestController
@RequestMapping("/auth")
@RequiredArgsConstructor
public class AuthController {

    private final AuthenticationManager authenticationManager;
    private final JwtService jwtService;
    private final UsuarioRepository userRepository;
    private final RolRepository roleRepository;
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

}


