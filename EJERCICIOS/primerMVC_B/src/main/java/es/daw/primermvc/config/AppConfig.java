package es.daw.primermvc.config;

import es.daw.primermvc.controller.ProductoController;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

/**
 * GESTIÓN CENTRALIZADA DE BEANS:
 * Al usar la anotación @Configuration y declarar un método anotado con @Bean,
 * estás definiendo un bean administrado por el contenedor de Spring.
 * INYECCIÓN DE DEPENDENCIAS:
 * En ProductoController, inyectamos RestTemplate mediante @Autowired.
 * Esto requiere que exista un bean de tipo RestTemplate en el contexto de Spring.
 * AppConfig se encarga de crear ese bean (sigue el principio de inversión de control (IoC)). *
 *
 */
@Configuration
public class AppConfig {

    @Value("${daw.api.url}")
    public String BASE_URL;

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }

    @Bean
    public WebClient webClient(WebClient.Builder builder){
        return builder
                .baseUrl(BASE_URL)
                .build();
    }

}
