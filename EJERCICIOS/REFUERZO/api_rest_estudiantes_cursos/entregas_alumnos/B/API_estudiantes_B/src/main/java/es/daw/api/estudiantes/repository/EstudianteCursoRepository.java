package es.daw.api.estudiantes.repository;

import es.daw.api.estudiantes.entity.EstudianteCurso;
import es.daw.api.estudiantes.entity.EstudianteCursoId;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteCursoRepository extends JpaRepository<EstudianteCurso, EstudianteCursoId> {

}
