# Implementar un API REST utilizando Spring Boot para gestionar un sistema de Estudiantes y Cursos

Crea un proyecto Spring Boot llamado NOMBRE_API_ESTUDIANTES... PENDIENTE DEFINIR CÓMO TRABAJAR EN GITHUB!!!!

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

- GET /estudiantes → Listar todos los estudiantes.
- GET /estudiantes/{id} → Obtener un estudiante por su ID.
- POST /estudiantes → Crear un nuevo estudiante.
- PUT /estudiantes/{id} → Actualizar un estudiante existente.
- PATCH /estudiantes/{id} → Actualiza parcialmente los atributos de un estudiante, como su nombre o correo electrónico.
- DELETE /estudiantes/{id} → Eliminar un estudiante.
  
**Cursos:**

El id se corresponde con del código del curso.

- GET /cursos → Listar todos los cursos.
- GET /cursos/{id} → Obtener un curso por su ID.
- POST /cursos → Crear un nuevo curso.
- PUT /cursos/{id} → Actualizar un curso existente.
- DELETE /cursos/{id} → Eliminar un curso.
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
INSERT INTO estudiantes (nombre, email) VALUES 
('Juan Pérez', 'juan.perez@example.com'),
('María López', 'maria.lopez@example.com'),
('Carlos Gómez', 'carlos.gomez@example.com');

INSERT INTO cursos (nombre, descripcion) VALUES 
('Matemáticas', 'Curso de matemáticas básicas'),
('Programación Java', 'Curso introductorio a Java y Spring Boot'),
('Historia', 'Historia mundial desde 1900');

```

Vamos a añadir unos campos únicos en cada tabla para poder referenciar a los distintos objetos de forma única sin usar la clave primaria id:

```
-- Agregar el campo NIA a la tabla estudiantes
ALTER TABLE estudiantes
ADD COLUMN NIA CHAR(8) NOT NULL UNIQUE;

-- Agregar el campo código a la tabla curso
ALTER TABLE curso
ADD COLUMN codigo CHAR(3) NOT NULL;

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

## Validaciones

A la hora de persistir en la base de datos debes de tener en cuenta las siguientes validaciones:
- El nombre del estudiante no puede estar vacío.
- El email no puede estar vacío
- Debe proporcionar un email válido (@Email)
- El nombre del curso no puede estar vacío.
- El nombre del curso debe tener entre 3 y 50 caracteres.
- La descripción no debe exceder los 255 caracteres.


## Logging de errores

Pendiente de definir....

## Fichero de propiedades externo.

configuration.properties ....
