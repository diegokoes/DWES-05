package es.daw.api.estudiantes.controller;

import es.daw.api.estudiantes.dto.CursoDTO;
import es.daw.api.estudiantes.dto.EstudianteDTO;
import es.daw.api.estudiantes.entity.Curso;
import es.daw.api.estudiantes.entity.Estudiante;
import es.daw.api.estudiantes.entity.EstudianteCurso;
import es.daw.api.estudiantes.entity.EstudianteCursoId;
import es.daw.api.estudiantes.repository.CursoRepository;
import es.daw.api.estudiantes.repository.EstudianteCursoRepository;
import es.daw.api.estudiantes.repository.EstudianteRepository;
import jakarta.validation.constraints.Size;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/estudiantes")
public class EstudiantesController {

    // --------- INYECCIÓN DE REPOSITORIOS --------
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private final EstudianteCursoRepository estudianteCursoRepository;

    @Autowired
    public EstudiantesController(CursoRepository cursoRepository, EstudianteRepository estudianteRepository, EstudianteCursoRepository estudianteCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.estudianteCursoRepository = estudianteCursoRepository;
    }

    // -------- ENDPOINT DE SOLO ESTUDIANTES ----------

    // --------- ENDPOINT INSCRIPCIONES -----------
    @PostMapping("/{idEstudiante}/cursos/{idCurso}")
    public ResponseEntity<Void> inscribirEstudianteCurso(
            @PathVariable("idEstudiante") Long idEstudiante,
            @PathVariable("idCurso") Long idCurso) {

        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        //Estudiante estudiante = estudianteRepository.findById(idEstudiante).orElse(null);
        Optional<Estudiante> estudiante = estudianteRepository.findById(idEstudiante);
        if (estudiante.isEmpty()) {
            return ResponseEntity.notFound().build();
        }

        // Si pasa por aquí, el curso y estudiante existen en la bd y puedo inscribir

        // De esta forma, me da error porque la fecha_inscripción es nula
//        curso.addEstudiante(estudiante.get());
//        cursoRepository.save(curso);

        EstudianteCursoId estudianteCursoId = new EstudianteCursoId(idEstudiante, idCurso);

        // Comprobar si existe la inscripción (si el estudiante X se ha inscrito en el curso Y)
        if (estudianteCursoRepository.existsById(estudianteCursoId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Crear una nueva inscripción con la fecha actual
        EstudianteCurso inscripcion = new EstudianteCurso(estudiante.get(),curso);

        // Guardar la inscripción
        estudianteCursoRepository.save(inscripcion);

        System.out.println("***** LISTADO DE CURSOS DEL ESTUDIANTE ******");
        System.out.println(estudiante);
        estudiante.get().getCursos().forEach(System.out::println);
        System.out.println("*********************************************");

        return ResponseEntity.ok().build();

    }

    @PostMapping("/inscribir/{nia}/cursos/{codigo}")
    public ResponseEntity<Void> inscribirEstudianteCurso(
            @PathVariable
            @Size(min = 8, max = 8, message ="El NIA debe tener exactamente 8 caracteres") String nia,
            @PathVariable("codigo")
            @Size(min = 3, max = 3, message = "El código delcurso debe tener exactamente 8 caracteres") String codigoCurso
    ){


    Curso curso = cursoRepository.findByCodigo(codigoCurso).orElse(null);
    if (curso == null) {
        return ResponseEntity.notFound().build();
    }

    Estudiante estudiante = estudianteRepository.findByNia(nia).orElse(null);
    if (estudiante == null) {
        return ResponseEntity.notFound().build();
    }

    // ---- todo igual ----

        // MEJORA! HACER UN SERVICIO DE INSCRIPCIÓN
        // PASAMOS AL SERVICIO EL ESTUDIANTE Y EL CURSO

        EstudianteCursoId estudianteCursoId = new EstudianteCursoId(estudiante.getId(), curso.getId());

        // Comprobar si existe la inscripción (si el estudiante X se ha inscrito en el curso Y)
        if (estudianteCursoRepository.existsById(estudianteCursoId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build();
        }

        // Crear una nueva inscripción con la fecha actual
        EstudianteCurso inscripcion = new EstudianteCurso(estudiante,curso);

        // Guardar la inscripción
        estudianteCursoRepository.save(inscripcion);

        System.out.println("***** LISTADO DE CURSOS DEL ESTUDIANTE ******");
        System.out.println(estudiante);
        estudiante.getCursos().forEach(System.out::println);
        System.out.println("*********************************************");

        return ResponseEntity.ok().build();


    }


    // GET /estudiantes/{nia}
    // Este es el JSON a devolver
        /*
        {
            "nia": "87654321",
            "nombre": "Ana López",
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
          }
         */

    @GetMapping("/{nia}")
    public ResponseEntity<EstudianteDTO>  getEstudianteByNia(
            @PathVariable String nia){

        // Compruebo si el estudiantes existe. Si no es así, devuelvo un 404
        Estudiante estudiante = estudianteRepository.findByNia(nia).orElse(null);

        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }
        // ---------------------------------------
        // Observa los constructores de los DTO. Además de los proporcionados por Lombok (vacío y con todos los atributos),
        // hemos creado constructores solo con los atributos principales de cada entidad.
        

        // PRIMERA FORMA
        // Monto la información principal del estudiante
//        EstudianteDTO estudianteDTO = new EstudianteDTO(
//                estudiante.getNia(),
//                estudiante.getNombre(),
//                estudiante.getEmail(),
//                null
//        );

//        EstudianteDTO estudianteDTO = new EstudianteDTO(
//                estudiante.getNia(),
//                estudiante.getNombre(),
//                estudiante.getEmail()
//        );
//         estudianteDTO.setCursos(cursos);

        // SEGUNDA FORMA
        // Monto los cursos incritos por el estudiante
        List<CursoDTO> cursos = estudiante.getCursos().stream()
                //.map(c -> new CursoDTO(c.getCodigo(),c.getNombre(),c.getDescripcion(),null))
                .map(c -> new CursoDTO(c.getCodigo(),c.getNombre(),c.getDescripcion()))
                .toList();


        EstudianteDTO estudianteDTO2 = new EstudianteDTO(
                estudiante.getNia(),
                estudiante.getNombre(),
                estudiante.getEmail(),
                cursos
        );

        return new ResponseEntity<>(estudianteDTO2, HttpStatus.OK);


    }





}
