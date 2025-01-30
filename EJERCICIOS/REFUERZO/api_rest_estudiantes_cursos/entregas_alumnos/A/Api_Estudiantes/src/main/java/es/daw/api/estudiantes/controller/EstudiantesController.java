package es.daw.api.estudiantes.controller;

import es.daw.api.estudiantes.entity.Curso;
import es.daw.api.estudiantes.entity.Estudiante;
import es.daw.api.estudiantes.repository.CursoRepository;
import es.daw.api.estudiantes.repository.EstudianteRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/estudiantes")
public class EstudiantesController {

    // --------- INYECCIÓN DE REPOSITORIOS --------
    private final CursoRepository cursoRepository;
    private final EstudianteRepository estudianteRepository;

    @Autowired
    public EstudiantesController(CursoRepository cursoRepository, EstudianteRepository estudianteRepository) {
        this.cursoRepository = cursoRepository;
        this.estudianteRepository = estudianteRepository;
    }

    // -------- ENDPOINT DE SOLO ESTUDIANTES ----------

    // --------- ENDPOINT INSCRIPCIONES -----------
    @PostMapping("/{idEstudiante}/cursos/{idCurso}")
    public ResponseEntity<Void> inscribirEstudianteCurso(@PathVariable Long idEstudiante, @PathVariable Long idCurso) {

        Curso curso = cursoRepository.findById(idCurso).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        Estudiante estudiante = estudianteRepository.findById(idEstudiante).orElse(null);
        if (estudiante == null) {
            return ResponseEntity.notFound().build();
        }

        curso.addEstudiante(estudiante);
        cursoRepository.save(curso);
        return ResponseEntity.ok().build();

    }




}
