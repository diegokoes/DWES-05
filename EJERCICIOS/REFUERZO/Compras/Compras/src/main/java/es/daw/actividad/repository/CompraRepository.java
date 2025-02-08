package es.daw.actividad.repository;

import es.daw.actividad.entity.Compra;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByFechaBetween(LocalDateTime fechaInicial, LocalDateTime  fechaFinal);
}
