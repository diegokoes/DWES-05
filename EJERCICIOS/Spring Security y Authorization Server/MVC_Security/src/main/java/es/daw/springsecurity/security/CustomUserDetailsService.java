package es.daw.springsecurity.security;

import es.daw.springsecurity.entity.User;
import es.daw.springsecurity.repository.UserRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.ArrayList;

@Service
@Slf4j
public class CustomUserDetailsService implements UserDetailsService {

    //@Autowired
    private final UserRepository userRepository; // Tu repositorio para acceder a los usuarios
    @Autowired
    public CustomUserDetailsService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        // Lógica para cargar el usuario desde la base de datos
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Username {} not found", username);
                    return new UsernameNotFoundException("Usuario no encontrado");
                });

        System.out.println("*************** LOAD USER BY USERNAME *****************");
        System.out.println("Usuario: " + user.getUsername());
        System.out.println("Roles: " + user.getRoles()); // Verificar que los roles existen
        System.out.println("********************************************************");


        // Retorna un objeto UserDetails con la información del usuario
        //return new org.springframework.security.core.userdetails.User(user.getUsername(), user.getPassword(), new ArrayList<>());

        return new org.springframework.security.core.userdetails.User(
                user.getUsername(),
                user.getPassword(),
                user.getAuthorities());

    }
}

