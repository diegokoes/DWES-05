package es.daw.api.estudiantes.repository;

import es.daw.api.estudiantes.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CursoRepository extends JpaRepository<Curso, Long> {
}
