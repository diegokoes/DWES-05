package es.daw.api.estudiantes.controller;

import es.daw.api.estudiantes.dto.CursoDTO;
import es.daw.api.estudiantes.dto.EstudianteDTO;
import es.daw.api.estudiantes.entity.Curso;
import es.daw.api.estudiantes.repository.CursoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/cursos")
public class CursosController {

    private final CursoRepository cursoRepository;

    @Autowired
    public CursosController(CursoRepository cursoRepository) {
        this.cursoRepository = cursoRepository;
    }

    @GetMapping("/{codigo}")
    public ResponseEntity<CursoDTO> obtenerCurso(@PathVariable String codigo){

        // Obtener el curso por su codigo
        Curso curso = cursoRepository.findByCodigo(codigo).orElse(null);
        if (curso == null) {
            return ResponseEntity.notFound().build();
        }

        /*
{
  "codigo": "101",
  "nombre": "Matemáticas Básicas",
  "descripcion": "Curso introductorio",
  "estudiantesInscritos": [
    { "nia": "12345678", "nombre": "Juan Pérez", "email: asdfasd" },
    { "nia": "87654321", "nombre": "Ana López" , "email: dasdasdfas}
  ]
}
         */
        // Obtener los estudiantes inscritos en el curso
        List<EstudianteDTO> estudiantesInscritos = curso.getEstudiantesInscritos().stream()
                .map( estudiante -> new EstudianteDTO(
                        estudiante.getNia(),
                        estudiante.getNombre(),
                        estudiante.getEmail())
                ).toList();

        CursoDTO cursoDTO = new CursoDTO(
                curso.getCodigo(),
                curso.getNombre(),
                curso.getDescripcion(),
                estudiantesInscritos
        );

        return ResponseEntity.ok(cursoDTO);
    }

}
