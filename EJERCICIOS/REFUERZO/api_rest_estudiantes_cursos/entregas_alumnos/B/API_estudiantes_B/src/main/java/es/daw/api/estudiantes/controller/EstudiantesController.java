package es.daw.api.estudiantes.controller;

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

@RestController
@RequestMapping("/estudiantes")
//POST /estudiantes/{idEstudiante}/cursos/{idCurso} → Inscribir un estudiante en un curso.
public class EstudiantesController {

    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;
    private final EstudianteCursoRepository estudianteCursoRepository;

    @Autowired
    public EstudiantesController(CursoRepository cursoRepository,
                                 EstudianteRepository estudianteRepository,
                                 EstudianteCursoRepository estudianteCursoRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
        this.estudianteCursoRepository = estudianteCursoRepository;
    }

    @PostMapping("/{idEstudiante}/cursos/{idCurso}")
    public ResponseEntity<Void> inscribirEstudianteCurso(@PathVariable Long idCurso, @PathVariable Long idEstudiante) {

        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        Estudiante estudiante = estudianteRepository.findById(idEstudiante).orElse(null);

        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }

        // ME VA A DAR UN ERROR PORQUE LA FECHA DE INSCRIPCIÓN NO PUEDE SER NULO
//        curso.addEstudiante(estudiante);
//        cursoRepository.save(curso);

        // Verificar si ya existe la inscripción
        //EstudianteCursoId estudianteCursoId = new EstudianteCursoId(idCurso, idEstudiante);
        EstudianteCursoId estudianteCursoId = new EstudianteCursoId(curso.getId(), estudiante.getId());

        if(estudianteCursoRepository.existsById(estudianteCursoId)) {
            return ResponseEntity.status(HttpStatus.CONFLICT).build(); // ya inscrito //409
        }

        // Crear una nueva inscripción con la fecha actual
        EstudianteCurso inscripcion = new EstudianteCurso(estudiante,curso);

        // Guardar la inscripción
        estudianteCursoRepository.save(inscripcion);

        // ------------

        // StackOverFlow. Infinito
//        System.out.println("******** LISTADO DE CURSOS DEL ESTUDIANTE *********");
//        System.out.println(estudiante);
//        estudiante.getCursos().forEach(System.out::println);
//        System.out.println("*************************************************");

//        curso.getEstudiantesInscritos().add(estudiante);
//        estudiante.getCursos().add(curso);

        return ResponseEntity.ok().build();



    }

    @PostMapping("/inscribir/{nia}/cursos/{codigo}")
    public ResponseEntity<Void> inscribirEstudianteCurso(
            @PathVariable
            @Size(min = 8, max = 8, message = "El NIA debe tener exactamente 8 caracteres") String nia,
            @PathVariable("codigo")
            @Size(min = 3, max = 3, message = "El código del curso debe tener exactamente 3 caracteres") String codigoCurso
            ) {


        // pendiente completar....

        return ResponseEntity.ok().build();

    }

}
