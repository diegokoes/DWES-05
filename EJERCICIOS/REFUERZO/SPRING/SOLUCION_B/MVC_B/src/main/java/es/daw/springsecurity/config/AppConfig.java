package es.daw.springsecurity.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class AppConfig {

    @Value("${daw.api.url.api}")
    private String apiURL;

    @Value("${daw.api.url.auth}")
    private String authUrl;

    // Definir múltiples webClient
    @Bean
    public WebClient webClientAuth(WebClient.Builder builder) {
        return builder
                .baseUrl(authUrl)
                .build();
    }

    @Bean
    public WebClient webClientEventos(WebClient.Builder builder) {
        return builder
                .baseUrl(apiURL)
                .build();
    }

}

