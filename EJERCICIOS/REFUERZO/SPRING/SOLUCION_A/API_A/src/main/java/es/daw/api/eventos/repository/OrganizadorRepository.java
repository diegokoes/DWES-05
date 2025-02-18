package es.daw.api.eventos.repository;

import es.daw.api.eventos.entity.Organizador;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import java.util.Optional;

@RepositoryRestResource(path = "organizadores")
public interface OrganizadorRepository extends JpaRepository<Organizador, Long> {
    Optional<Organizador> findByCodigo(String codigo);
}
