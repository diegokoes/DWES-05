package es.daw.api.eventos.repository;

import es.daw.api.eventos.entity.Evento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "events")
public interface EventoRepository extends JpaRepository<Evento, Long> {
    Optional<Evento> findByCodigo(String codigo);
}
