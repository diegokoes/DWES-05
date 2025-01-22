package es.daw.primercrud.repository;

import es.daw.primercrud.entity.Producto;

import jakarta.transaction.Transactional;
import org.springframework.data.domain.Example;
import org.springframework.data.jpa.repository.JpaRepository;

import java.math.BigInteger;
import java.util.Optional;

public interface ProductoRepository extends JpaRepository<Producto, BigInteger> {
    // puedo añadir otros métodos abstractos, default...
    Producto findProductoById(Long id);

    Optional<Producto> findProductoBySku(String sku);

    boolean existsBySku(String sku);

    @Transactional
    void deleteProductoBySku(String sku);

}
