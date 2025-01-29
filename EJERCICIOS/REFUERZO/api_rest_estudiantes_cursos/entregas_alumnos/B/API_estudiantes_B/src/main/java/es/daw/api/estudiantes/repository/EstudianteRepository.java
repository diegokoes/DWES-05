package es.daw.api.estudiantes.repository;

import es.daw.api.estudiantes.entity.Curso;
import es.daw.api.estudiantes.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
    public Optional<Estudiante> findByNia(String nia);
}
