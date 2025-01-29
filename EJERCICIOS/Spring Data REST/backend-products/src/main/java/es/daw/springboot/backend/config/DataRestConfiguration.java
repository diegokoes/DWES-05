package es.daw.springboot.backend.config;

import es.daw.springboot.backend.entity.Product;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.rest.core.config.RepositoryRestConfiguration;
import org.springframework.data.rest.webmvc.config.RepositoryRestConfigurer;
import org.springframework.web.servlet.config.annotation.CorsRegistry;

@Configuration
public class DataRestConfiguration implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Exponer los IDs de las entidades en los endpoints de Spring Data REST
        // Se devuelve en el JSON el id
        config.exposeIdsFor(Product.class);

        // Configurar CORS globalmente para todos los endpoints de Spring Data REST
        cors.addMapping("/**")  // Permite CORS para todas las rutas de Spring Data REST
                .allowedOrigins("http://localhost:5173","http://localhost:4200")  // Dominios permitidos
                .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
                .allowedHeaders("*");  // Permite todos los encabezados
    }
}


