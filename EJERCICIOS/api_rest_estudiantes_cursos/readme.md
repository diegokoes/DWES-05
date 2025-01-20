# Implementar un API REST utilizando Spring Boot para gestionar un sistema de Estudiantes y Cursos

Crea un proyecto Spring Boot llamado **NOMBRE_API_ESTUDIANTES**... PENDIENTE DEFINIR CÓMO TRABAJAR EN GITHUB!!!!

## Requisitos

Los estudiantes podrán inscribirse en varios cursos, y los cursos podrán tener varios estudiantes (relación @ManyToMany).

El API REST debe permitir realizar operaciones CRUD (Crear, Leer, Actualizar y Eliminar) tanto para estudiantes como para cursos. 

Además, se debe implementar un endpoint para inscribir estudiantes en cursos.

### Relaciones:
- Estudiante (id, nombre, email).
- Curso (id, nombre, descripción).
- Relación ManyToMany: un estudiante puede estar inscrito en varios cursos, y un curso puede tener varios estudiantes.


### Endpoints del API:

**Estudiantes:**

El id se corresponde con el NIA del estudiante.

- GET /estudiantes → Listar todos los estudiantes. Incluir el curso al que está inscrito cada estudiante (si aplica). Por ejemplo:
  ```
  [
    {
      "nia": "12345678",
      "nombre": "Juan Pérez",
      "edad": 20,
      "correo": "juan.perez@example.com",
      "cursos": [{
        "codigo": "101",
        "nombre": "Matemáticas Básicas",
        "descripcion": "Curso introductorio de matemáticas"
      }]
    },
    {
      "nia": "87654321",
      "nombre": "Ana López",
      "edad": 22,
      "correo": "ana.lopez@example.com",
      "cursos": [{
          "codigo": "102",
          "nombre": "Física General",
          "descripcion": "Curso introductorio de física"
        },
        {
          "codigo": "101",
          "nombre": "Matemáticas Básicas",
          "descripcion": "Curso introductorio de matemáticas"
        }
      ]
    },
    {
      "nia": "45678912",
      "nombre": "Carlos Gómez",
      "edad": 19,
      "correo": "carlos.gomez@example.com",
      "cursos": null
    }
  ]
  
  ```
  
- GET /estudiantes/{id} → Obtener un estudiante por su ID.
- POST /estudiantes → Crear un nuevo estudiante.
- PUT /estudiantes/{id} → Actualizar un estudiante existente.
- PATCH /estudiantes/{id} → Actualiza parcialmente los atributos de un estudiante, como su nombre o correo electrónico.
- DELETE /estudiantes/{id} → Eliminar un estudiante. ¿Qué sucede si el estudiante está inscrito a un curso?
  
**Cursos:**

El id se corresponde con del código del curso.

- GET /cursos → Listar todos los cursos.
- GET /cursos/{id} → Obtener un curso por su ID. Incluir los estudiantes inscritos en ese curso (si aplica). Por ejemplo:
  ```
  {
    "codigo": "101",
    "nombre": "Matemáticas Básicas",
    "descripcion": "Curso introductorio",
    "estudiantesInscritos": [
      { "nia": "12345678", "nombre": "Juan Pérez" },
      { "nia": "87654321", "nombre": "Ana López" }
    ]
  }
  ```
- POST /cursos → Crear un nuevo curso.
- PUT /cursos/{id} → Actualizar un curso existente.
- DELETE /cursos/{id} → Eliminar un curso. ¿Qué sucede si el curso tienes estudiantes inscritos?
- PATCH /cursos/{id} → Actualiza parcialmente los atributos de un curso, como su nombre o descripción.
  
**Inscripciones:**

El idEstudiante es el NIA y el idCurso es el código del curso.

- POST /estudiantes/{idEstudiante}/cursos/{idCurso} → Inscribir un estudiante en un curso.
- GET /estudiantes/inscripciones → Listado de inscripciones
- GET /estudiantes/inscripciones/{idCurso} → Listado de inscripciones de un curso concreto
- DELETE /estudiantes/inscripciones/{idEstudiante}/{idCurso} → Dar de baja a un estudiante del curso (desinscribir)
  

## Script de base de datos H2

A la base de datos la llamaremos **estudiantes (estudiantes.mv.db)**.

```
-- Tabla Estudiantes
CREATE TABLE estudiantes (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    email VARCHAR(255) UNIQUE NOT NULL
);

-- Tabla Cursos
CREATE TABLE cursos (
    id BIGINT AUTO_INCREMENT PRIMARY KEY,
    nombre VARCHAR(255) NOT NULL,
    descripcion VARCHAR(255)
);
-- Tabla intermedia para la relación ManyToMany
CREATE TABLE estudiantes_cursos (
    estudiante_id BIGINT NOT NULL,
    curso_id BIGINT NOT NULL,
    PRIMARY KEY (estudiante_id, curso_id),
    FOREIGN KEY (estudiante_id) REFERENCES estudiantes(id) ON DELETE CASCADE,
    FOREIGN KEY (curso_id) REFERENCES cursos(id) ON DELETE CASCADE
);

-- Datos de prueba
INSERT INTO estudiantes (nombre, email,nia) VALUES 
('Juan Pérez', 'juan.perez@example.com','12345678'),
('María López', 'maria.lopez@example.com','12345679'),
('Carlos Gómez', 'carlos.gomez@example.com','12345671');

INSERT INTO cursos (nombre, descripcion,codigo) VALUES 
('Matemáticas', 'Curso de matemáticas básicas','MAT'),
('Programación Java', 'Curso introductorio a Java y Spring Boot','PRO'),
('Historia', 'Historia mundial desde 1900','HIS');

```
Como algunos de vosotros ya tiene la base de datos creada y he detectado la necesidad de añadir unos nuevos campos, os paso unos scripts para actualizarla.

- Vamos a añadir unos campos únicos en cada tabla para poder referenciar a los distintos objetos de forma única sin usar la clave primaria id.
- Vamos a añadir a la tabla intermedia un campo, llamado fecha_inscripción.

```
-- Agregar el campo NIA a la tabla estudiantes
ALTER TABLE estudiantes
ADD COLUMN NIA CHAR(8) NOT NULL UNIQUE;

-- Agregar el campo código a la tabla curso
ALTER TABLE cursos
ADD COLUMN codigo CHAR(3) NOT NULL;

-- Agregar el campo fecha_inscripcion
ALTER TABLE estudiantes_cursos
ADD COLUMN fecha_inscripcion DATE NOT NULL;


```
## Entities

En el caso de **relaciones manytomany con campos en la tabla intermedia (fechaInscripcion)**, es necesario crear una clase entidad de dicha tabla de esta manera:

```
@Entity
@Table(name = "estudiantes_cursos")
public class EstudianteCurso {

    @EmbeddedId
    private EstudianteCursoId id;

    @ManyToOne
    @MapsId("estudianteId")
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @MapsId("cursoId")
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    // Getters y Setters

}
```
La clave compuesta EstudianteCursoId se define como @Embeddable:

```
@Embeddable
public class EstudianteCursoId implements Serializable {

    private Long estudianteId;
    private Long cursoId;

    ...
}
```

## DTOs

Crea los DTO correspondientes. Simplemente que no contengan el id.

Además crea **InscripcionDTO** para guardar información combinada del estudiante y curso:
- nombreEstudiante
- emailEstudiante
- nombreCurso

El endpoint POST /estudiantes/{idEstudiante}/cursos/{idCurso} → Devolverá InscripciónDTO.
El endpoint GET /estudiantes/inscripciones → Devolverá un listado de todas las inscripciones

## Gestión de excepciones 

### EstudianteNotFoundException

Debes crear una excepción EstudianteNotFoundException que se lanzará cuando un estudiante no se encuentre en la base de datos.

Utilizando **@ControllerAdvice**, captura la excepción y devuelve objeto **ErrorDetailDTO.**


### ErrorDetailDTO

Debe contener esta información y usaremos el principio de inmutabilidad:

```
    private final String message;
    private final Map<String, String> details;
    private final LocalDateTime timestamp;
    private final int statusCode;
```

## Manejo de errores (code status)

Como:

- **404 Not Found:** Si no se encuentra un estudiante, curso o inscripción.
- **400 Bad Request:** Si los datos de entrada no son válidos (por ejemplo, un NIA o código de curso inválido).
- **409 Conflict:** Si intentas crear duplicados (como inscribir dos veces al mismo estudiante en el mismo curso).

## Validaciones

A la hora de persistir en la base de datos debes de tener en cuenta las siguientes validaciones:
- El nombre del estudiante no puede estar vacío.
- El email no puede estar vacío
- Debe proporcionar un email válido (@Email)
- El nombre del curso no puede estar vacío.
- El nombre del curso debe tener entre 3 y 50 caracteres.
- La descripción no debe exceder los 255 caracteres.


## Fichero de propiedades externo.

En un fichero de propiedades externo, definir variables de configuración.

Completad la clase ApiConfig:

```
@Configuration
@ConfigurationProperties(prefix = "config.api")
@PropertySources({
        @PropertySource(value="classpath:configuration.properties", encoding = "UTF-8"),
})
@Data
@NoArgsConstructor
public class ApiConfig {
    ...
}
```

## Logging de errores

Pendiente de definir....

## Paginación

Pendiente....

## Seguridad y Autorización

Para restricciones de acceso, implementaremos autenticación y autorización (usando Spring Authorization Server OAuth 2.1 y Spring Security JWT).

Pendiente...

## Documentación

Proporciona documentación clara (por ejemplo, usando Swagger/OpenAPI) para que los desarrolladores puedan interactuar fácilmente con tu API.

Pendiente...
