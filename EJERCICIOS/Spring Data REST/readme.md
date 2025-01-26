# Spring Data REST

 Spring Data Rest automáticamente expone una API REST basada en los repositorios de Spring Data (como CrudRepository o JpaRepository).
 
 Esto significa que no necesitas crear manualmente un controlador con anotaciones como @RestController y definir los típicos endpoints (GET, POST, PUT, DELETE) porque Spring Data REST lo hace por ti.
 
 ___

# Forma de usarlo: RepositoryRestResource + CORS

CORS (Cross-Origin Resource Sharing) es un mecanismo de seguridad implementado en los navegadores que permite o restringe que los recursos web (como API REST) sean solicitados desde un dominio distinto al de la propia página web. 

Cuando una aplicación en un dominio hace una solicitud (como una petición HTTP) a un servidor en otro dominio, el navegador bloquea esta solicitud por razones de seguridad, a menos que el servidor indique explícitamente que permite dicha solicitud desde ese origen distinto.

## Configuración de CORS solo para el repositorio

 ```
@CrossOrigin(origins = "http://localhost:5173")
@RepositoryRestResource(path = "products")
public interface ProductRepository extends CrudRepository<Product, Long> {}

 ```

La anotación **@RepositoryRestResource(path = "products")** expone automáticamente un conjunto de endpoints REST en la ruta /products.

Los métodos básicos como:

- GET /products → Listar todos los productos.
- GET /products/{id} → Obtener un producto por su ID.
- POST /products → Crear un nuevo producto.
- PUT /products/{id} → Actualizar un producto existente.
- DELETE /products/{id} → Eliminar un producto por su ID.

Se generan de manera automática.

## Configuración de CORS a nivel global


```
@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        // Exponer los IDs de las entidades en los endpoints de Spring Data REST
        config.exposeIdsFor(Product.class);

        // Configurar CORS globalmente para todos los endpoints de Spring Data REST
        cors.addMapping("/**")  // Permite CORS para todas las rutas de Spring Data REST
            .allowedOrigins("http://localhost:5173")  // Dominios permitidos
            .allowedMethods("GET", "POST", "PUT", "DELETE")  // Métodos permitidos
            .allowedHeaders("*");  // Permite todos los encabezados
    }
}


```

- Se está configurando el CORS (Cross-Origin Resource Sharing) para permitir peticiones desde el origen http://localhost:5173 (frontend en React).
- **config.exposeIdsFor(Product.class)** asegura que el ID de las entidades Product se incluya en las respuestas JSON de la API, ya que por defecto Spring Data REST oculta los IDs.
- No es estrictamente necesaria, ya que puedes controlar CORS directamente usando @CrossOrigin en los controladores o repositorios como lo estás haciendo en tu ProductRepository.
- Es útil si necesitas una configuración global de CORS para todas las rutas de Spring Data REST, especialmente si tienes varios repositorios y quieres asegurarte de que todas las rutas de Spring Data REST (como /products, /orders, etc.) permiten CORS desde tu frontend.

___

## ¿Cuándo usar Spring Data REST en lugar de un @RestController?

- Los endpoints REST son simples y siguen el CRUD estándar.
- Quieres minimizar el código repetitivo para manejar la creación, lectura, actualización y eliminación de recursos.
- No necesitas personalizar demasiado los endpoints.

