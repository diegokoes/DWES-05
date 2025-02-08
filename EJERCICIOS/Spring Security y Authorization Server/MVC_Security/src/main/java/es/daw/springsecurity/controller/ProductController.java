package es.daw.springsecurity.controller;

import es.daw.springsecurity.entity.Product;
import es.daw.springsecurity.service.ProductService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/productos")
public class ProductController {

    private final ProductService productService;

    @Autowired
    public ProductController(ProductService productService) {
        this.productService = productService;
    }

    @GetMapping
    public String listarProductos(Model model) {
        List<Product> productos = productService.findAll();
        model.addAttribute("productos", productos);
        return "productos/lista"; // Thymeleaf template para listar productos
    }

    @GetMapping("/nuevo")
    public String mostrarFormularioDeNuevoProducto(Model model) {
        model.addAttribute("producto", new Product());
        return "productos/nuevo"; // Thymeleaf template para el formulario
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute("producto") Product producto) {
        productService.save(producto);
        return "redirect:/productos";
    }
}
