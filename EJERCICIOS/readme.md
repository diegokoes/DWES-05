# EJERCICIO 1: RestController, CRUD de productos, MVC

Vamos a partir de la base de datos H2 usada en la práctica de la primera evaluación **tienda_practica.mv.db**

Llama a tu proyecto **primerCrud**.

Aprenderemos a:
- Configurar el acceso a la base de datos (no trabajaremos en memoria).
- Crear la entidad de persistencia Producto.
- Crear el repositorio asociado a Producto.
- Crear el controlador Rest con diferentes endpoints. **(revisar la teoría en el aula virtual de la UT06, "Métodos HTTP para servicios RESTful")**
- Realizar operaciones CRUD sobre la tabla Productos.

Sigue las instrucciones del profesor...

___

## Ampliación 1: mejorar el ejercicio 1 usando ProductoDTO

Después de haber aprendido la utilidad de un DTO **(revisar la teoría en el aula virtual de la UT05, "DTO - Data Transfer Object")** vamos a crear un DTO adecuado para la entidad Producto.

Los atributos necesarios a transmitir serán el nombre, precio y sku.

Pasos a realizar:

- Diseña la clase **ProductoDTO**.
- Mapea la entidad producto con el dto. Implementa una clase @Service adecuada.
- Modifica el API Rest de productos para usar ProductoDTO en lugar de directamente el Entity.

___

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

**¿Y si no pasamos algún parámetro del request? ¿Y si pasamos un parámetro con un valor que no se puede convertir a numérico?**

Vamos a revisar la ampliación 4...

___

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

Fíjate que cuando anotas la interfaz con @Mapper, le estás indicando a MapStruct que genere una implementación de esta interfaz automáticamente durante el proceso de compilación.
Además, el uso de INSTANCE elimina la necesidad de crear manualmente una instancia del mapper. MapStruct sigue el patrón singleton


- Usar el Mapper:
    - Forma 1:
        ```
        ....
        List<Producto> productos = productoRepository.findAll();
    
        //Usar MapStruct para convertir la lista de productos
        return productos.stream()
                .map(ProductoMapper.INSTANCE::toProductoDTO)
                .collect(Collectors.toList());

        ... 
        ```
    - Forma 2:
      ```
          List<Producto> productos = productoRepository.findAll();
          return productos.stream()
            .map(producto -> ProductoMapper.INSTANCE.toProductoDTO(producto))
            .collect(Collectors.toList());
      
      ```
    

Si necesitásemos agregar lógica más compleja podríamos usar métodos personalizados o combinar MapStruct con expresiones. Por ejemplo:

```
@Mapping(source = "sku", target = "codigo")
@Mapping(target = "nombreCompleto", expression = "java(producto.getNombre() + ' ' + producto.getDescripcion())")
ProductoDTO toProductoDTO(Producto producto);
```

___

## Ampliación 4: manejar excepciones

**1. Crear un DTO para guardar información de errores:**

Se llamará **ErrorDTO**  y tendrá estos dos atributos. Usa Lombok para completar el resto de código.

```
    private String message;
    private String details;
```

**2. Crear @ControllerAdvice para manejar excepciones:**

ControllerAdvice centraliza el manejo de excepciones en toda la aplicación.

```
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDTO> handleNumberFormatException(NumberFormatException ex) {
        ErrorDTO error = new ErrorDTO(
                "Invalid number format",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // Puedes añadir más métodos @ExceptionHandler para manejar otras excepciones.
}

```

**3. Añadir al controlador REST la gestión del error:**

Fíjate en este código de ejemplo y vamos a ver cómo aplicarlo a nuestro controlador rest:

```
    @GetMapping("/parse-int")
    public String parseInteger(@RequestParam String number) {
        int parsedNumber = Integer.parseInt(number); // Puede lanzar NumberFormatException
        return "Parsed number: " + parsedNumber;
    }
```

**4. Añadir gestión genérica de excepciones no previstas:**

Agregamos un método en la clase GlobalExceptionHandler para Exception:

```
@ExceptionHandler(Exception.class)
public ResponseEntity<ErrorDTO> handleGenericException(Exception ex) {
    ErrorDTO error = new ErrorDTO(
            "Internal Server Error",
            ex.getMessage()
    );
    return new ResponseEntity<>(error, HttpStatus.INTERNAL_SERVER_ERROR);
}

```

___

## Ampliación 5: validaciones

Es necesario añadir la dependencia **Spring Boot Starter Validation**:

```
        <dependency>
            <groupId>org.springframework.boot</groupId>
            <artifactId>spring-boot-starter-validation</artifactId>
        </dependency>
```

Estas son las anotaciones más comunes:

![image](https://github.com/user-attachments/assets/fbe1bbbe-2f6c-4fa9-8ebf-b2208e6f48f5)


Vamos a añadir validaciones automáticas a ProductoDTO:

```
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;

    @Min(value = 1, message = "El precio debe ser un número positivo")
    private int precio;

    @NotBlank(message = "El SKU es obligatorio")
    private String sku;
```

En el controlador rest vamos a añadir la validación de productoDTO. Observa:

```
@PostMapping("/add")
    public String add(@Valid @RequestBody ProductoDTO productoDTO) {
              ...
    }
```

Ejemplo de otras validaciones:

![image](https://github.com/user-attachments/assets/cb5168ba-e896-49fc-b17d-d9731ee5feea)


___

## Ampliación 6: configuración personalizada

Podemos añadir parámetros de configuración directamente en el archivo **application.properties**. Observa:

```
spring.application.name=primerCrud

# Configuración de H2
spring.datasource.url=jdbc:h2:file:~/tienda_practica;AUTO_SERVER=TRUE
#spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driver-class-name=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

#configuraciones personalizadas
config.daw.code=666
config.daw.message=primer controlador REST
```

Para acceder a dichos valores desde una clase java de nuestro proyecto, por ejemplo en **ProductoController.java:**

```
    // ----------------------------------
    // CONFIGURACIÓN PERSONALIZADA
    @Value("${config.daw.code}")
    private String code_conf;
    @Value("${config.daw.message}")
    private String message_conf;
    //-------------------------------------
```

Vamos a crear dos endpoints para realizar las pruebas y ver que obtenemos los valores correctamente:

```
    @GetMapping("/values-conf")
    public Map<String,Object> values(){
        Map<String,Object> json = new HashMap<>();
        json.put("code",code_conf);
        json.put("message",message_conf);
        return json;
    }

    @GetMapping("/values-conf2")
    public Map<String,Object> values(@Value("${config.daw.code}") String code, @Value("${config.daw.message}") String message){
        Map<String,Object> json = new HashMap<>();
        json.put("code",code_conf);
        json.put("message",message_conf);
        return json;
    }

```
Como mejora, vamos a cambiar el Map por un objeto DTO con los parámetros de configuración y usaremos un archivo de properties externo.

### Agregando otros archivos properties personalizados para las configuraciones

Además de usar un fichero de propiedades externo, usaremos DTO y @ConfigurationProperties.

Sigue los pasos:

**DTO DawResponse con Lombok**

```
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, equals, hashCode y toString automáticamente.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacío.
public class DawResponse {
    private String code;
    private String message;
}
```

**Clase DawConfig con @ConfigurationProperties**

La clase que representa las propiedades:

```
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;
import lombok.Data;

@Component
@ConfigurationProperties(prefix = "config.daw")
@Data // Genera getters, setters, equals, hashCode y toString.
public class DawConfig {
    private String code;
    private String message;
}
```

**Creamos un controlador nuevo, específico para leer la configuración:**

```
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DawController {

    private final DawConfig dawConfig;

    @Autowired
    public DawController(DawConfig dawConfig) {
        this.dawConfig = dawConfig;
    }

    @GetMapping("/values-conf")
    public DawResponse values() {
        return new DawResponse(dawConfig.getCode(), dawConfig.getMessage());
    }

}
```

**Creamos el archivo de propiedades externo:**

En: src/main/resources/config/mi-config.properties

```
# Archivo externo: mi-config.properties
config.daw.code=666
config.daw.message=Primer controlador REST con acentos y eñes
```

___

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

En Spring, el objeto RestTemplate debe ser gestionado por el contenedor de Spring para que pueda ser inyectado con @Autowired **(revisar la teoría en el aula virtual de la UT05, "Inversión de Control (IoC) vs Inyección de despendecias (DI)").**

Esto se hace declarando un Bean de RestTemplate en tu configuración de Spring.

Esto le indica a Spring que debe crear y gestionar una instancia de RestTemplate, que luego estará disponible para ser inyectada.

Crea la clase **AppConfig** en el paquete config de tu proyecto:


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

## Ampliación 1: creación de logs por consola y en archivos

Vamos a añadir trazas a logs por consola y en ficheros en el servidor usando SLF4J y Logback **(revisar la teoría en el aula virtual de la UT05, "Logging en Java".**

Los logs se imprimirán en la consola y se guardarán en un archivo en la carpeta logs en el directorio donde se ejecuta la aplicación.

### Configurar logs desde application.properties (opción básica)

Si no necesitas configuraciones avanzadas como las anteriores, puedes configurar logs básicos desde el archivo application.properties.

Por ejemplo, puedes habilitar logs en un archivo simple con:

```
# Habilitar logs en un archivo
logging.file.name=logs/application.log

# Tamaño máximo del archivo antes de rotar
logging.file.max-size=10MB

# Número máximo de archivos de respaldo
logging.file.total-size-cap=100MB

# Nivel de logs global
logging.level.root=INFO

# Configurar niveles para paquetes específicos
logging.level.org.springframework=DEBUG
logging.level.com.mi.paquete=TRACE
```

### Configuración básica en logback-spring.xml

Necesitas personalizar un archivo de configuración de Logback. Crea un archivo llamado **logback-spring.xml en el directorio src/main/resources.**

Aquí tienes un ejemplo básico de configuración:

```
<configuration>
    <!-- Appender para escribir en la consola -->
    <appender name="CONSOLE" class="ch.qos.logback.core.ConsoleAppender">
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Appender para escribir en un archivo -->
    <appender name="FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <!-- Ruta del archivo donde se guardarán los logs -->
        <file>logs/application.log</file>

        <!-- Configuración de la política de rotación -->
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <!-- Archivo con la fecha para cada día -->
            <fileNamePattern>logs/application.%d{yyyy-MM-dd}.log</fileNamePattern>
            <!-- Mantener solo los últimos 30 días de logs -->
            <maxHistory>30</maxHistory>
        </rollingPolicy>

        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
    </appender>

    <!-- Configurar el nivel de logs -->
    <root level="INFO">
        <!-- Enviar logs a la consola -->
        <appender-ref ref="CONSOLE" />
        <!-- Enviar logs al archivo -->
        <appender-ref ref="FILE" />
    </root>
</configuration>

```

- **Appender para consola (CONSOLE):** Sigue enviando logs a la consola para facilitar el desarrollo.
- **Appender para archivo (FILE):**
    - Los logs se escribirán en un archivo llamado logs/application.log.
    - Los archivos de log se rotarán diariamente (cada día se crea un nuevo archivo con la fecha en el nombre, por ejemplo, application.2025-01-06.log).
    - Solo se conservarán los últimos 30 días de logs gracias a la propiedad <maxHistory>30</maxHistory>.
 
### Configuración avanzada: logs separados por niveles

Si deseas que los logs se guarden en diferentes archivos según el nivel (INFO, ERROR, etc.), puedes usar configuraciones más avanzadas:

```
<configuration>
    <!-- Appender para logs de INFO y superior -->
    <appender name="INFO_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/info.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/info.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>INFO</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- Appender para logs de ERROR -->
    <appender name="ERROR_FILE" class="ch.qos.logback.core.rolling.RollingFileAppender">
        <file>logs/error.log</file>
        <rollingPolicy class="ch.qos.logback.core.rolling.TimeBasedRollingPolicy">
            <fileNamePattern>logs/error.%d{yyyy-MM-dd}.log</fileNamePattern>
            <maxHistory>30</maxHistory>
        </rollingPolicy>
        <encoder>
            <pattern>%d{yyyy-MM-dd HH:mm:ss} [%thread] %-5level %logger{36} - %msg%n</pattern>
        </encoder>
        <filter class="ch.qos.logback.classic.filter.LevelFilter">
            <level>ERROR</level>
            <onMatch>ACCEPT</onMatch>
            <onMismatch>DENY</onMismatch>
        </filter>
    </appender>

    <!-- Nivel de logs global -->
    <root level="INFO">
        <appender-ref ref="INFO_FILE" />
        <appender-ref ref="ERROR_FILE" />
    </root>
</configuration>
```

- Los logs de nivel INFO, WARN, y DEBUG se guardarán en logs/info.log.
- Los logs de nivel ERROR se guardarán en logs/error.log.
- Los logs rotarán diariamente y se conservarán solo los últimos 30 días.


