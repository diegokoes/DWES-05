package es.daw.primercrud.repository;

import es.daw.primercrud.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;

public interface ProductoRepository extends JpaRepository<Producto, BigInteger> {
    // puedo añadir otros métodos abstractos, default...
    Producto findProductoById(Long id);

}
