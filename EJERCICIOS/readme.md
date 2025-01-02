# EJERCICIO 1: RestController, CRUD de productos

Vamos a partir de la base de datos H2 usada en la práctica de la primera evaluación **tienda_practica.mv.db**

Llama a tu proyecto **primerCrud**.

Aprenderemos a:
- Configurar el acceso a la base de datos (no trabajaremos en memoria).
- Crear la entidad de persistencia Producto.
- Crear el repositorio asociado a Producto.
- Crear el controlador Rest.
- Realizar operaciones CRUD.

Sigue las instrucciones del profesor...

# EJERCICIO 2: Implementar aplicación Spring MVC + Thymeleaf + CRUD de productos

Utilizaremos los endpoints REST para realizar las operaciones CRUD (Crear, Leer, Actualizar, Eliminar).

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

Vamos a añadir **RestTemplate** a una nueva clase de configuración **@Configuration**. 

En Spring, el objeto RestTemplate debe ser gestionado por el contenedor de Spring para que pueda ser inyectado con @Autowired.

Esto se hace declarando un Bean de RestTemplate en tu configuración de Spring.

Esto le indica a Spring que debe crear y gestionar una instancia de RestTemplate, que luego estará disponible para ser inyectada.

Crea la calse **AppConfig** en el paquete config de tu proyecto:


```
@Configuration
public class AppConfig {

    @Bean
    public RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
```

**Ten en cuenta que:**

- El API REST ya maneja el CRUD de productos.
- El controlador MVC realiza peticiones HTTP a tu API REST usando RestTemplate para realizar las operaciones CRUD.
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
Con el controlador MVC que interactúa a través de RestTemplate con tu API REST, ahora necesitamos ajustar las vistas de Thymeleaf para que puedas ver y manejar los productos.

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

