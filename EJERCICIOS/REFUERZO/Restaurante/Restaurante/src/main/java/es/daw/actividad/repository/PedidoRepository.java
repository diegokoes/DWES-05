package es.daw.actividad.repository;

import es.daw.actividad.entity.Pedido;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDateTime;
import java.util.List;

public interface PedidoRepository extends JpaRepository<Pedido,Long> {
    List<Pedido> findByFechaBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}
