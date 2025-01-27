package es.daw.api.gestionexcepciones.service;

import es.daw.api.gestionexcepciones.dto.ProductoDTO;
import es.daw.api.gestionexcepciones.entity.Producto;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    public Optional<ProductoDTO> convert2ProductoDTO(Producto producto){

        return Optional.ofNullable(new ProductoDTO(producto.getNombre(), producto.getSku(), producto.getPrecio()));


    }

    public Optional<Producto> convert2Producto(ProductoDTO productoDTO){
        return Optional.ofNullable(productoDTO)
                .map(dto -> {
                    Producto producto = new Producto();
                    producto.setNombre(dto.getNombre());
                    producto.setPrecio(dto.getPrecio());
                    producto.setSku(dto.getSku());
                    return producto;
                });

    }
}
