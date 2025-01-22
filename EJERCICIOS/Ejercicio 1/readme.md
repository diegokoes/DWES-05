# EJERCICIO 1: RestController, CRUD de productos

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

Es necesario añadir la dependencia **Spring Boot Starter Validation (Bean Validation)**:

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
    @NotBlank(message = "El SKU es obligatorio")
    @Size(min = 4, max = 4, message = "El SKU debe tener exactamente 4 caracteres")
    private String sku;
    @Min(value = 100, message = "El precio debe superior a 99")
    private int precio;
```

En el controlador rest vamos a añadir la validación de productoDTO. Observa:

```
@PostMapping("/add")
    public String add(@Valid @RequestBody ProductoDTO productoDTO) {
              ...
    }
```
Cuando Spring encuentra @Valid, ejecuta las validaciones de ProductoDTO antes de llamar al método. 

Si alguna validación falla, Spring lanza una excepción (MethodArgumentNotValidException) y devolverá automáticamente un código de estado HTTP 400 Bad Request y un mensaje de error que describe las validaciones fallidas.

Por ejemplo, si intentas enviar un JSON con datos inválidos:

```
{
    "id": null,
    "nombre": "",
    "precio": -5,
    "descripcion": "Esto es una descripción demasiado larga que excede el límite de 200 caracteres. Lorem ipsum dolor sit amet, consectetur adipiscing elit. Sed do eiusmod tempor incididunt ut labore et dolore magna aliqua.",
    "fechaExpiracion": "2023-01-01"
}
```

```
{
    "timestamp": "2025-01-09T10:30:00",
    "status": 400,
    "errors": [
        "El ID del producto no puede ser nulo",
        "El nombre del producto es obligatorio",
        "El precio debe ser mayor a 0",
        "La descripción no puede tener más de 200 caracteres",
        "La fecha de expiración debe ser en el futuro"
    ]
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
    public Map<String,String> values(){
        Map<String,String> json = new HashMap<>();
        json.put("code",code_conf);
        json.put("message",message_conf);
        return json;
    }


    @GetMapping("/values-conf2")
    public Map<String,String> values(@Value("${config.daw.code}") String code, @Value("${config.daw.message}") String message){
        Map<String,String> json = new HashMap<>();
        json.put("code",code);
        json.put("message",message);
        return json;
    }
```
Como mejora, vamos a cambiar el Map por un objeto DTO con los parámetros de configuración y usaremos un archivo de properties externo.

### Agregando otros archivos properties personalizados para las configuraciones

Además de usar un fichero de propiedades externo, usaremos DTO y @ConfigurationProperties.

Sigue los pasos:

**Creamos el archivo de propiedades externo:**

En: src/main/resources/mi-config.properties

```
# Archivo externo: mi-config.properties
config.daw.code=pruebaCode
config.daw.message=Mensaje personalizado
```

**Configuramos el uso de dicho fichero de propiedades externo:**

En application.properties añadimos:

```
#Esto carga tu archivo mi-config.properties junto con otros archivos de configuración.
spring.config.import=classpath:mi-config.properties
```

También podría añadirse a la clase marcada como @Configuration:

```
@PropertySources({
        @PropertySource(value="classpath:values.properties", encoding = "UTF-8"),
})
```

**DTO DawResponse con Lombok**

Es el objeto que devolverá el endpoint con los valores de los parámetros de configuración:

```

@Data // Genera getters, setters, equals, constructor con solo campos requeridos, hashCode y toString automáticamente.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacío.
public class DawResponseDTO {
    private String code;
    private String message;
}
```

Ten en cuenta que Jackson neesita deserializar de DTO a JSON y requiere un constructor sin argumento o constructores con todos los campos, por tanto poner los atributos final puede complicar dicha serialización.

**Clase DawConfig (en el paquete config) con @ConfigurationProperties**

La clase que representa las propiedades:

```
@Configuration
@ConfigurationProperties(prefix = "config.daw")
@Data
@NoArgsConstructor
public class DawConfig {
    private String code;
    private String message;
}
```

**Creamos un controlador nuevo, específico para leer la configuración:**

```
@RestController
public class DawController {

    @Autowired
    private DawConfig dawConfig;

    @GetMapping("/values-conf")
    public DawResponseDTO values() {
        return new DawResponseDTO(dawConfig.getCode(), dawConfig.getMessage());
    }

}
```

