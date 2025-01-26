# Spring Data REST

 Spring Data Rest automáticamente expone una API REST basada en los repositorios de Spring Data (como CrudRepository o JpaRepository).
 
 Esto significa que no necesitas crear manualmente un controlador con anotaciones como @RestController y definir los típicos endpoints (GET, POST, PUT, DELETE) porque Spring Data REST lo hace por ti.
 
# Forma de usarlo

### Repositorio

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

### Configuración de CORS

```
@Configuration
public class DataRestConfig implements RepositoryRestConfigurer {
    @Override
    public void configureRepositoryRestConfiguration(RepositoryRestConfiguration config, CorsRegistry cors) {
        config.exposeIdsFor(Product.class);
    }
}

```

- Se está configurando el CORS (Cross-Origin Resource Sharing) para permitir peticiones desde el origen http://localhost:5173 (frontend en React).
- **config.exposeIdsFor(Product.class)** asegura que el ID de las entidades Product se incluya en las respuestas JSON de la API, ya que por defecto Spring Data REST oculta los IDs.

___

## ¿Cuándo usar Spring Data REST en lugar de un @RestController?

- Los endpoints REST son simples y siguen el CRUD estándar.
- Quieres minimizar el código repetitivo para manejar la creación, lectura, actualización y eliminación de recursos.
- No necesitas personalizar demasiado los endpoints.

