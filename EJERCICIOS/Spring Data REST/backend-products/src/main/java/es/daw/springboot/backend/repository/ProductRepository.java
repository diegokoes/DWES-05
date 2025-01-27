package es.daw.springboot.backend.repository;

import es.daw.springboot.backend.entity.Product;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.web.bind.annotation.CrossOrigin;

@RepositoryRestResource(path="products")
@CrossOrigin(origins = {"http://localhost:5173","http://localhost:4200"})
public interface ProductRepository extends CrudRepository<Product, Long> {
}
