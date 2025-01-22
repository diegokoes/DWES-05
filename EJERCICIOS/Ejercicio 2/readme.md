
# EJERCICIO 2: Implementar aplicación Spring MVC + Thymeleaf + CRUD de productos

**Recuerda!!!!**

- El MVC es un patrón de diseño del software que permite una separción entre la lógica de negocio de una aplicación y la vista que se le presenta al usuario.
- Utiliza como intermediario a un controlador (recibe las requests) que se encarga de tomar la decisión de cómo interactúan la vista (frontend) y el modelo entre si (nuestras clases java).

Utilizaremos los endpoints REST, del ejercicio 1,para realizar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar).

Vamos a crear un nuevo proyecto llamado **primerMVC** que reutilizará el API Rest creado en el ejercicio anterior.

**Configura este proyecto para que se ejecute en el puerto 8081. Para ello edita application.properties y añade:**

```
server.port=8081
```

Vamos a aprender a conectar el **frontend (Thymeleaf)** con esos endpoints para que puedas hacer uso de ellos en el navegador, en lugar de interactuar con la API REST de forma aislada.

## Pasos a realizar

- Implementar el controlador MVC para que haga uso de los endpoints REST que ya tienes.
- Crear las vistas Thymeleaf para realizar las operaciones CRUD mediante formularios y enlaces (en resources/templates).
- Configurar la conexión entre el controlador MVC y el servicio REST.


## 1. Implementar el controlador MVC

El controlador MVC (ProductoController.java) realizará las peticiones HTTP a tu API REST y procesará las respuestas. 

Crea la clase **AppConfig** en el paquete config de tu proyecto. Será una clase de configuración para poder inyectar objetos **WebClient, parte de Spring WebFlux**.

Podríamos usar también **RestTemplate**, pero es más moderno usar **WebClient** que funciona tanto de forma síncrona como reactiva.

```
@Configuration
public class AppConfig {

    @Value("${daw.api.url}")
    private String url;

    @Bean
    public WebClient webClient(WebClient.Builder builder) {
        return builder
                .baseUrl(url) // Configuración global opcional
                .build();
    }
}
```

**Ten en cuenta que:**

- El API REST ya maneja el CRUD de productos.
- El controlador MVC realiza peticiones HTTP a tu API REST usando WebClient para realizar las operaciones CRUD.
- Las vistas Thymeleaf permiten interactuar con el controlador y visualizar la lista de productos, crear nuevos, editar y eliminar productos.

**El proyecto quedará de la siguiente manera:**

```
src/main/java/es/daw/primermvc
    ├── controller
    │      └── ProductoController.java
    ├── model
    │      └── Producto.java
    ├── config
    │      └── AppConfig.java
    └── PrimermvcApplication.java

``` 

## 2. Actualizar las vistas Thymeleaf


### 2.1. Listar productos

Crea **list.html en resources/templates**:

```
<!DOCTYPE html>
<html xmlns:th="http://www.thymeleaf.org">
<head>
    <title>Lista de Productos</title>
</head>
<body>
<h1>Lista de Productos</h1>
<table>
    <thead>
    <tr>
        <th>Nombre</th>
        <th>Precio</th>
        <th>SKU</th>
    </tr>
    </thead>
    <tbody>
    <tr th:each="producto : ${productos}">
        <td th:text="${producto.nombre}"></td>
        <td th:text="${producto.precio}"></td>
        <td th:text="${producto.sku}"></td>
    </tr>
    </tbody>
</table>
</body>
</html>
```

Ejecuta el proyecto: http://localhost:8081/productos

Chuleta Thymeleaf: https://github.com/profeMelola/DWES-05-2024-25/blob/main/APOYO_TEORIA/Thymeleaf.md

### 2.2. Editar producto

### 2.3. Borrar producto

### 2.4. Crear nuevo producto


## Ampliación 1: gestión de errores


