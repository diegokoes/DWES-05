package es.daw.api.estudiantes.repository;

import es.daw.api.estudiantes.entity.Estudiante;
import org.springframework.data.jpa.repository.JpaRepository;

public interface EstudianteRepository extends JpaRepository<Estudiante, Long> {
}
