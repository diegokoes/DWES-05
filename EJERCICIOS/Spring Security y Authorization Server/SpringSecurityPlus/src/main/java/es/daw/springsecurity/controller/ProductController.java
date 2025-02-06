package es.daw.springsecurity.controller;

import es.daw.springsecurity.entity.Product;
import es.daw.springsecurity.service.ProductService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/products")
@RequiredArgsConstructor
public class ProductController {

    private final ProductService productService;

    // 🔹 Listar todos los productos (accesible por USER y ADMIN)
    @GetMapping
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    //@PreAuthorize("hasAnyAuthority('ADMIN', 'USER')") // Si en JWT no estuvieran los roles co el prefijo ROLE
    public ResponseEntity<List<Product>> list() {

        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        System.out.println("********** DETALLES DE AUTENTICACIÓN **********");
        System.out.println("Usuario autenticado: " + authentication.getName());
        System.out.println("Roles:");
        authentication.getAuthorities().forEach(auth -> System.out.println(auth.getAuthority()));
        System.out.println("************************************************");


        List<Product> productos = productService.findAll();
        if (productos.isEmpty()) {
            return ResponseEntity.noContent().build(); // 204 No Content si no hay productos
        }
        return ResponseEntity.ok(productos); // 200 OK con la lista de productos
    }

    // 🔹 Obtener un producto por ID
    @GetMapping("/{id}")
    @PreAuthorize("hasAnyRole('ADMIN', 'USER')")
    public ResponseEntity<Product> getById(@PathVariable Long id) {
        Optional<Product> product = productService.findById(id);
        return product.map(ResponseEntity::ok) // 200 OK si el producto existe
                .orElseGet(() -> ResponseEntity.notFound().build()); // 404 Not Found si no existe
    }

    // 🔹 Crear un producto (solo ADMIN puede hacerlo)
    @PostMapping
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> create(@RequestBody Product product) {
        if (product.getName() == null || product.getPrice() <= 0) {
            return ResponseEntity.badRequest().body(null); // 400 Bad Request si los datos no son válidos
        }
        Product savedProduct = productService.save(product);
        return ResponseEntity.status(HttpStatus.CREATED).body(savedProduct); // 201 Created con el producto creado
    }

    // 🔹 Actualizar un producto (solo ADMIN)
    @PutMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Product> update(@PathVariable Long id, @RequestBody Product productDetails) {
        Optional<Product> existingProduct = productService.findById(id);

        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found si no existe
        }

        Product updatedProduct = existingProduct.get();
        updatedProduct.setName(productDetails.getName());
        updatedProduct.setPrice(productDetails.getPrice());

        Product savedProduct = productService.save(updatedProduct);
        return ResponseEntity.ok(savedProduct); // 200 OK con el producto actualizado
    }

    // 🔹 Eliminar un producto (solo ADMIN)
    @DeleteMapping("/{id}")
    @PreAuthorize("hasRole('ADMIN')")
    public ResponseEntity<Void> delete(@PathVariable Long id) {
        Optional<Product> existingProduct = productService.findById(id);

        if (existingProduct.isEmpty()) {
            return ResponseEntity.notFound().build(); // 404 Not Found si no existe
        }

        productService.delete(id);
        return ResponseEntity.noContent().build(); // 204 No Content si se elimina correctamente
    }
}
