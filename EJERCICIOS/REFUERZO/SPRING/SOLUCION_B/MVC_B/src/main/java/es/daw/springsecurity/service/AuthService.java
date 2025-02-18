package es.daw.springsecurity.service;

import es.daw.springsecurity.model.AuthRequest;
import es.daw.springsecurity.model.AuthResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class AuthService {

    private final WebClient webClientAuth;

    public AuthService(@Qualifier("webClientAuth") WebClient webClientAuth) {
        this.webClientAuth = webClientAuth;
    }

    /**
     *
     * @return
     */
    public String obtenerToken() {
        AuthRequest request = new AuthRequest("admin", "admin");
        AuthResponse response = webClientAuth.post()
                //.uri("/")
                .header("Content-Type", "application/json")
                .bodyValue(request)
                .retrieve()
                .bodyToMono(AuthResponse.class)
                .block();
        return response != null ? response.getToken() : "";
    }
}
