#  GET /cursos/{id} → Obtener un curso por su ID. Incluir los estudiantes inscritos en ese curso (si aplica). 

Ejemplo de Json de salida:

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
## Entidades

### Curso

```
@Entity
public class Curso {
    @Id
    private String codigo;

    private String nombre;
    private String descripcion;

    @ManyToMany
    @JoinTable(
        name = "curso_estudiante", 
        joinColumns = @JoinColumn(name = "codigo_curso"), 
        inverseJoinColumns = @JoinColumn(name = "nia_estudiante")
    )
    private List<Estudiante> estudiantesInscritos;

    // Getters y setters
}

```

### Estudiante

```
@Entity
public class Estudiante {
    @Id
    private String nia;

    private String nombre;

    @ManyToMany(mappedBy = "estudiantesInscritos")
    private List<Curso> cursos;

    // Getters y setters
}

```

## Solución con JPQL avanzado

```
public interface CursoRepository extends JpaRepository<Curso, String> {

    @Query("SELECT c FROM Curso c LEFT JOIN FETCH c.estudiantesInscritos WHERE c.codigo = :codigo")
    Optional<Curso> findCursoConEstudiantesPorCodigo(@Param("codigo") String codigo);
}

```

Con **LEFT JOIN FETCH** te aseguras que los estudiantes asociados con un curso se carguen en la misma consulta y evitar múltiples consultas adicionales.

- LEFT JOIN: Crea una unión entre la entidad Curso y su relación con estudiantesInscritos. Esto asegura que:
    - Todos los cursos serán devueltos, incluso si no tienen estudiantes inscritos (los estudiantes serían NULL en este caso).
    - Si solo usáramos un INNER JOIN, los cursos sin estudiantes no aparecerían en el resultado.
- FETCH: Se utiliza para cargar los estudiantes relacionados inmediatamente, es decir, cargar la relación estudiantesInscritos de forma ansiosa en lugar de dejarla como perezosa (lazy loading).
    - Sin FETCH, JPA simplemente devolvería el curso, pero no cargaría la lista de estudiantes automáticamente.
    - Con FETCH, los datos de los estudiantes también se obtienen en la misma consulta.

Evita un problema conocido como el **N+1 Query Problem**, en el que JPA realizaría una consulta para el curso y luego una consulta adicional por cada estudiante relacionado.

## Solución sin JPQL

```
public interface CursoRepository extends JpaRepository<Curso, String> {
    Optional<Curso> findByCodigo(String codigo);
}
```

En el controlador, por ejemplo:

```
@GetMapping("/{codigo}")
public ResponseEntity<CursoDTO> obtenerCurso(@PathVariable String codigo) {
    Optional<Curso> curso = cursoRepository.findByCodigo(codigo);

    if (curso.isPresent()) {
        Curso c = curso.get();
        
        // Construir el DTO
        CursoDTO cursoDTO = new CursoDTO();
        cursoDTO.setCodigo(c.getCodigo());
        cursoDTO.setNombre(c.getNombre());
        cursoDTO.setDescripcion(c.getDescripcion());

        List<EstudianteDTO> estudiantesDTO = c.getEstudiantesInscritos().stream()
            .map(e -> {
                EstudianteDTO estudianteDTO = new EstudianteDTO();
                estudianteDTO.setNia(e.getNia());
                estudianteDTO.setNombre(e.getNombre());
                return estudianteDTO;
            })
            .collect(Collectors.toList());

        cursoDTO.setEstudiantesInscritos(estudiantesDTO);

        return ResponseEntity.ok(cursoDTO);
    }

    return ResponseEntity.notFound().build(); // o gestión de una excepción personalizada....
}

```

- En una relación @ManyToMany, el FetchType por defecto es LAZY, aunque no lo especifiques explícitamente en la anotación.
- La propiedad estudiantesInscritos NO será cargada automáticamente cuando consultes un curso. En cambio, se cargará cuando accedas a ella explícitamente en el código (lo que desencadenará una consulta adicional a la base de datos).
- Esta carga adicional (es decir, hacer una consulta extra para los estudiantes) puede hacer que las consultas sean menos eficientes, especialmente si tienes muchas relaciones.
- Si tu aplicación necesita optimizar la carga de los estudiantes y evitar múltiples consultas adicionales (es decir, si los estudiantes son muchos y no quieres que Spring haga una segunda consulta a la base de datos), podrías configurar la carga de la relación para que sea eager (inmediata). 
    - Esto significa que los estudiantes se cargarían al mismo tiempo que el curso, sin necesidad de una consulta adicional.
    - Ten en cuenta que la relación siempre se carga automáticamente al consultar la entidad principal, sin importar si realmente necesitas los datos de esa relación.
    - Evítalo para relaciones grandes. Es menos eficiente que usar la JPQL LEFT JOIN FETCH.

```
@ManyToMany(fetch = FetchType.EAGER)
@JoinTable(
    name = "curso_estudiante", 
    joinColumns = @JoinColumn(name = "codigo_curso"), 
    inverseJoinColumns = @JoinColumn(name = "nia_estudiante")
)
private List<Estudiante> estudiantesInscritos;

```