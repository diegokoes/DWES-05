package es.daw.springboot.backend.controller;

import es.daw.springboot.backend.entity.Product;
import es.daw.springboot.backend.repository.ProductRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/products")  // Define el prefijo para los endpoints del controlador
public class ProductController {

    private final ProductRepository productRepository;

    @Autowired
    public ProductController(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    @GetMapping("/expensive")
    public ResponseEntity<List<Product>> getExpensiveProducts(@RequestParam Double precio) {
        List<Product> expensiveProducts = productRepository.findByPriceGreaterThan(precio);
        return ResponseEntity.ok(expensiveProducts);
    }

}