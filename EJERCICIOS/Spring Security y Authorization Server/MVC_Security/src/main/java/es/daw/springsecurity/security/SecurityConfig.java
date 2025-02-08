package es.daw.springsecurity.security;

import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.authentication.dao.DaoAuthenticationProvider;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity // Activa la seguridad en la aplicación y permite personalizar la configuración de Spring Security.
@RequiredArgsConstructor
//@EnableMethodSecurity // habilita @PreAuthorize
public class SecurityConfig {

    private final CustomUserDetailsService userDetailsService;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/", "/home", "/register","/h2-console/**").permitAll()
                        .requestMatchers("/productos/nuevo").hasRole("ADMIN")
                        .requestMatchers("/productos").hasAnyRole("USER", "ADMIN")
                                //.requestMatchers("/productos/**").authenticated()
                        .anyRequest().authenticated()
                )
                .formLogin(login -> login
                        .loginPage("/login") //Usa la URL /login como página de autenticación personalizada.
                        //.defaultSuccessUrl("/productos", true) // Redirige a /productos tras iniciar sesión.
                        .defaultSuccessUrl("/home", true) // Si el login es exitoso, va a home
                        .failureUrl("/login?error=true") // Si el login falla, redirige con ?error=true
                        .permitAll()
                )
                .logout(logout -> logout
                        .logoutUrl("/logout") // Define /logout como la URL para cerrar sesión.
                        .logoutSuccessUrl("/home") // Tras cerrar sesión, el usuario vuelve a /home.
                        .permitAll()
                ) //Cross-Site Request Forgery (CSRF) es un tipo de ataque que engaña a un usuario para que realice acciones en una aplicación web sin su consentimiento o conocimiento
                .csrf(csrf -> csrf.disable()) //Desactiva protección CSRF, H2-Console lo requiere.
                .headers(headers -> headers.frameOptions(f -> f.disable()))// frames de h2-console
                .exceptionHandling(exception -> exception.accessDeniedPage("/error"));
        return http.build();
    }

    /**
     * Crea un DaoAuthenticationProvider, que es el mecanismo estándar de autenticación basado en base de datos.
     * Usa CustomUserDetailsService para buscar los usuarios en la base de datos.
     * Usa PasswordEncoder para validar contraseñas encriptadas.
     * @param passwordEncoder
     * @return
     */
    @Bean
    public AuthenticationProvider authenticationProvider(PasswordEncoder passwordEncoder) {
        DaoAuthenticationProvider authProvider = new DaoAuthenticationProvider();
        authProvider.setUserDetailsService(userDetailsService);
        authProvider.setPasswordEncoder(passwordEncoder);
        return authProvider;
    }

    /**
     * Crea una instancia de AuthenticationManager, que es el responsable de manejar la autenticación en Spring Security.
     * Usa ProviderManager(authenticationProvider) para decirle que utilice el DaoAuthenticationProvider configurado antes.
     * @param authenticationProvider
     * @return
     */
    @Bean
    public AuthenticationManager authenticationManager(AuthenticationProvider authenticationProvider) {
        return new ProviderManager(authenticationProvider);
    }

    /**
     * Crea un encriptador de contraseñas (BCryptPasswordEncoder), que se usará para almacenar y validar contraseñas.
     * BCrypt es el estándar recomendado para seguridad en contraseñas.
     * @return
     */
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
