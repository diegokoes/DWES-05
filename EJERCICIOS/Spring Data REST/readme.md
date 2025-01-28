# Spring Data REST

 Spring Data Rest automáticamente expone una API REST basada en los repositorios de Spring Data (como CrudRepository o JpaRepository).
 
 Esto significa que no necesitas crear manualmente un controlador con anotaciones como @RestController y definir los típicos endpoints (GET, POST, PUT, DELETE) porque Spring Data REST lo hace por ti.
 
 ___

## Forma de usarlo: RepositoryRestResource + CORS

CORS (Cross-Origin Resource Sharing) es un mecanismo de seguridad implementado en los navegadores que permite o restringe que los recursos web (como API REST) sean solicitados desde un dominio distinto al de la propia página web. 

Cuando una aplicación en un dominio hace una solicitud (como una petición HTTP) a un servidor en otro dominio, el navegador bloquea esta solicitud por razones de seguridad, a menos que el servidor indique explícitamente que permite dicha solicitud desde ese origen distinto.

### Configuración de CORS solo para el repositorio

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

### Configuración de CORS a nivel global


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
### Datos de prueba

Crea en resources de tu proyecto un fichero llamado **import.sql**:

```
INSERT INTO products (name, description, price) VALUES ('Producto 1', 'Descripción 1', 100);
INSERT INTO products (name, description, price) VALUES ('Producto 2', 'Descripción 2', 200);
```


___

## ¿Cuándo usar Spring Data REST en lugar de un @RestController?

- Los endpoints REST son simples y siguen el CRUD estándar.
- Quieres minimizar el código repetitivo para manejar la creación, lectura, actualización y eliminación de recursos.
- No necesitas personalizar demasiado los endpoints.

___
## Sobreescribir o modificar endpoints generados automáticamente por Spring Data REST

Aunque Spring Data REST crea automáticamente los endpoints CRUD en base a las interfaces de repositorio (CrudRepository, JpaRepository, etc.), proporciona opciones para personalizarlos o reemplazarlos cuando sea necesario.

### Personalizar los endpoints con métodos en el repositorio

Puedes agregar métodos personalizados en la interfaz de repositorio para ampliar o modificar el comportamiento.

**Ejemplo: Añadir un método de consulta personalizada**

```
public interface ProductRepository extends CrudRepository<Product, Long> {

    // Consulta personalizada basada en el nombre
    List<Product> findByNameContainingIgnoreCase(String name);
}
```

Esto generará el siguiente endpoint:

```
GET http://localhost:8080/products/search/findByNameContainingIgnoreCase?name=example
```


### Sobrescribir métodos del CRUD con un @RestController

```
@RestController
@RequestMapping("/products")
public class ProductController {

    private final ProductRepository productRepository;

    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @PostMapping
    public ResponseEntity<Product> createProduct(@RequestBody Product product) {
        // Validaciones personalizadas o lógica adicional
        if (product.getPrice() < 0) {
            return ResponseEntity.badRequest().body(null);
        }
        Product savedProduct = productRepository.save(product);
        return ResponseEntity.ok(savedProduct);
    }
}
```

- La ruta POST /products gestionada por Spring Data REST será sobrescrita por tu implementación.
- Otros endpoints como GET /products o DELETE /products/{id} seguirán funcionando como antes.

### Utilizar @RepositoryRestController para personalización

Por ejemplo:

```
@RepositoryRestController
public class CustomProductController {

    private final ProductRepository productRepository;

    public CustomProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/products/expensive")
    public ResponseEntity<List<Product>> getExpensiveProducts() {
        List<Product> expensiveProducts = productRepository.findByPriceGreaterThan(1000L);
        return ResponseEntity.ok(expensiveProducts);
    }
}
```

Esto agrega un nuevo endpoint a los generados automáticamente:

```
GET http://localhost:8080/products/expensive

```

### Deshabilitar los endpoints generados automáticamente

```
@CrossOrigin(origins = {"http://localhost:5173"})
@RepositoryRestResource(path = "products", exported = true)
public interface ProductRepository extends CrudRepository<Product, Long> {

    @Override
    @RestResource(exported = false) // Desactiva DELETE
    void deleteById(Long id);

    @Override
    @RestResource(exported = false) // Desactiva POST
    <S extends Product> S save(S entity);
}

```
___

___

# EJERCICIO 1: backend productos + integración con cliente React y Angular

Sigue las indicaciones del profesor para crear la aplicación Spring Boot **backend-product**

## Pruebas

Si ejecutas **http://localhost:8080/** obtendrás el siguiente JSON:

```
{
  "_links" : {
    "products" : {
      "href" : "http://localhost:8080/products"
    },
    "profile" : {
      "href" : "http://localhost:8080/profile"
    }
  }
}

```
- **products:** Este enlace (http://localhost:8080/products) apunta al recurso de tus productos, que es el endpoint principal para gestionar la entidad Product. Puedes hacer:
    - GET http://localhost:8080/products → Para listar todos los productos.
    - POST http://localhost:8080/products → Para crear un nuevo producto.
    - GET http://localhost:8080/products/{id} → Para obtener un producto específico por su ID.
    - PUT, PATCH, DELETE, etc., según las operaciones soportadas.
- **profile:** Este enlace (http://localhost:8080/profile) apunta a la metadata del modelo. Proporciona detalles adicionales sobre las entidades y sus relaciones. Es parte del soporte de Spring Data REST para ALPS (Application-Level Profile Semantics).

## HATEOAS (solo teoría. La práctica queda fuera del ámbito del módulo)

HATEOAS (Hypermedia as the Engine os Application State) es un principio arquitectónico donde cada respuesta incluye enlaces relevantes para navegar por la API. 

Esto hace que la API sea autodescriptiva y más fácil de explorar, ya que el cliente puede seguir los enlaces sin necesidad de saber todos los endpoints de antemano.

Por ejemplo:

```
{
  "numero":1,
  "concepto":"informatica",
  "importe":700.0,
  "links":[
      {
        "rel":"self",
        "href":"http://localhost:8080/facturas/1"},
        {
          "rel":"lineas",
          "href":"http://localhost:8080/facturas/1/lineas"
        }
  ]
}
```

Spring proporciona herramientas específicas para facilitar esta tarea, en particular el módulo Spring HATEOAS.

Observa el uso de Link y EntityModel (fuera del alcance del curso):

```
@RestController
@RequestMapping("/products")
public class ProductController {

    @GetMapping("/{id}")
    public EntityModel<Product> getProduct(@PathVariable Long id) {
        Product product = findProductById(id); // Lógica para obtener el producto

        // Añadir enlaces
        EntityModel<Product> productModel = EntityModel.of(product,
                Link.of("/products/" + id, "self"),  // Enlace "self"
                Link.of("/products", "products"));  // Enlace al listado de productos

        return productModel;
    }
}

```

## EJERCICIO 2: integración con React y Angular

Tienes los dos proyectos en el repositorio.

Simplemente debes desplegarlos en tu entorno.

El objetivo no es entender el código de ambas aplicaciones front, sino ver cómo se comunican con el backend implementado en Spring.

## Pasos para ejecutar App React

### Instala Node.js

Para comprobar si tienes node instalado, ejecuta en consola:

```
node -v 

npm -v
```

Para que funcione posteriormente con Angular, instala la versión 18 LTS.

### Instala dependencias

```
npm install
```

Si da problemas de permisos, cambia dicha política temporalmente. Ejeucta:

```
Set-ExecutionPolicy -Scope Process -ExecutionPolicy Bypass
```

### Ejecuta el servidor de desarrollo

```
nmp run dev
```

### Abre la aplicación

http://localhost:5173/

## Pasos para ejecutar App Angular

Damos por hecho que ya tenemos Node.js

### Instalar Angular CLI

```
npm install -g @angular/cli
```

### Verifica que Angular CLI esté disponible

```
ng version
```
