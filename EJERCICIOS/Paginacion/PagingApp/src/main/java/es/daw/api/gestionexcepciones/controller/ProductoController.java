package es.daw.api.gestionexcepciones.controller;

import es.daw.api.gestionexcepciones.dto.ProductoDTO;
import es.daw.api.gestionexcepciones.entity.Producto;
import es.daw.api.gestionexcepciones.exception.ProductoNotFoundException;
import es.daw.api.gestionexcepciones.repository.ProductoRepository;
import es.daw.api.gestionexcepciones.service.ProductoService;
import jakarta.validation.constraints.Max;
import jakarta.validation.constraints.Min;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/productos")
@Validated // Habilita la validación de parámetros
public class ProductoController {

    private final ProductoService productoService;
    private final ProductoRepository productoRepository;

    @Autowired
    public ProductoController(ProductoService productoService, ProductoRepository productoRepository) {
        this.productoService = productoService;
        this.productoRepository = productoRepository;
    }

    @GetMapping
    public ResponseEntity<List<ProductoDTO>> findAll(){

        return ResponseEntity.ok(productoRepository.findAll()
                .stream()
                //.map( p -> productoService.convert2ProductoDTO(p))
                .map(productoService::convert2ProductoDTO)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList()
        );

    }



    @GetMapping("/find-sku/{sku}")
    public ResponseEntity<ProductoDTO> findBySku(@PathVariable String sku){
        Optional<Producto> productoOpt = productoRepository.findProductoBySku(sku);

        if (productoOpt.isPresent()) {
            ProductoDTO productoDTO = productoService.convert2ProductoDTO(productoOpt.get()).get();
            return ResponseEntity.ok(productoDTO); //200
        }

        return ResponseEntity.notFound().build(); //404
    }

    @GetMapping("/find-sku-ex/{sku}")
    public ResponseEntity<ProductoDTO> findBySkuEx(@PathVariable String sku){
        Producto producto = productoRepository.findProductoBySku(sku)
                .orElseThrow(() -> new ProductoNotFoundException(sku));

        ProductoDTO productoDTO = productoService.convert2ProductoDTO(producto).get();

        return ResponseEntity.ok(productoDTO);

//        Optional<Producto> productoOpt = productoRepository.findProductoBySku(sku);
//
//        if (productoOpt.isPresent()) {
//            ProductoDTO productoDTO = productoService.convert2ProductoDTO(productoOpt.get()).get();
//            return ResponseEntity.ok(productoDTO); //200
//        }
//
//        throw new ProductoNotFoundExcpetion(sku);
//        //return ResponseEntity.notFound().build(); //404
    }






}
