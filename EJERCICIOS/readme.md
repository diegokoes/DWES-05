# EJERCICIO 1: RestController, CRUD de productos, MVC

Vamos a partir de la base de datos H2 usada en la práctica de la primera evaluación **tienda_practica.mv.db**

Llama a tu proyecto **primerCrud**.

Aprenderemos a:
- Configurar el acceso a la base de datos (no trabajaremos en memoria).
- Crear la entidad de persistencia Producto.
- Crear el repositorio asociado a Producto.
- Crear el controlador Rest.
- Realizar operaciones CRUD.

Sigue las instrucciones del profesor...

## Ampliación 1: mejorar el ejercicio 1 usando ProductoDTO

Después de haber aprendido la utilidad de un DTO (revisar la teoría en el aula virtual de la UT05, "DTO - Data Transfer Object") vamos a crear un DTO adecuado para la entidad Producto.

Los atributos necesarios a transmitir serán el nombre, precio y sku.

Pasos a realizar:

- Diseña la clase **ProductoDTO**.
- Mapea la entidad producto con el dto. Implementa una clase @Service adecuada.
- Modifica el API Rest de productos para usar ProductoDTO en lugar de directamente el Entity.

## Ampliación 2: uso nativo de HttpServletRequest

Observa el siguiente código donde hay dos métodos para crear un producto en la base de datos.

En el segundo se usa directamente y de forma nativa HttpServletRequest:

```
    @PostMapping("/add")
    public String add(@RequestParam String nombre, @RequestParam int precio, @RequestParam String sku) {
        Producto producto = new Producto();
        producto.setNombre(nombre);
        producto.setPrecio(precio);
        producto.setSku(sku);
        productoRepository.save(producto);
        return "Added new product to repo!";
    }

    @PostMapping("/add-nativo")
    public String add(HttpServletRequest request) {
        ProductoDTO productoDTO = new ProductoDTO();
        productoDTO.setNombre(request.getParameter("nombre"));
        productoDTO.setSku(request.getParameter("sku"));
        productoDTO.setPrecio(Integer.parseInt(request.getParameter("precio")));

        productoService.convert2Producto(productoDTO).ifPresent(producto -> productoRepository.save(producto));

        return "Added new product to repo!";

    }
```
En la forma nativa:

¿Y si no pasamos algún parámetro del request? ¿Y si pasamos un parámetro con un valor que no se puede convertir a numérico?

Observa que con @RequestParam obtienes directamente el precio de tipo entero y no es necesario hacer un Integer.parseInt que fallará si llega a null o el valor no es convertible a numérico.

## Ampliación 3: uso de la librería MapStruct

Usar MapStruct para mapear entre una entidad y un DTO es una práctica muy eficiente, ya que automatiza la conversión y elimina la necesidad de escribir código repetitivo. 

### Pasos:

- Agregar la dependencia al proyecto.
- Crear el Mapper:
  ```
    import org.mapstruct.Mapper;
    import org.mapstruct.Mapping;
    import org.mapstruct.factory.Mappers;

    @Mapper
    public interface ProductoMapper {
        ProductoMapper INSTANCE = Mappers.getMapper(ProductoMapper.class);

        // Mapea la entidad Producto al DTO ProductoDTO
        @Mapping(source = "sku", target = "codigo")
        ProductoDTO toProductoDTO(Producto producto);

        // Mapea el DTO ProductoDTO a la entidad Producto
       @Mapping(source = "codigo", target = "sku")
        Producto toProducto(ProductoDTO productoDTO);
    }

  ```
- Usar el Mapper:
    ```
        ....
        List<Producto> productos = productoRepository.findAll();
    
        //Usar MapStruct para convertir la lista de productos
        return productos.stream()
                .map(ProductoMapper.INSTANCE::toProductoDTO)
                .collect(Collectors.toList());

        ... 
    ```
  

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

