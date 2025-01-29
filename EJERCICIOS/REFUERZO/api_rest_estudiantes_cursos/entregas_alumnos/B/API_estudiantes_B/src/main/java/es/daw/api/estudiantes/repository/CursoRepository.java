package es.daw.api.estudiantes.repository;

import es.daw.api.estudiantes.entity.Curso;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CursoRepository extends JpaRepository<Curso,Long> {
    public Optional<Curso> findByCodigo(String codigo);
}
