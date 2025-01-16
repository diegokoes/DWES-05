package es.daw.primercrud.service;

import es.daw.primercrud.dto.ProductoDTO;
import es.daw.primercrud.entity.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Optional;

@Service
public class ProductoService {

    public Optional<ProductoDTO> convert2ProductoDTO(Producto producto){

//        ProductoDTO productoDTO = new ProductoDTO();
//        productoDTO.setNombre(producto.getNombre());
//        productoDTO.setPrecio(producto.getPrecio());
//        productoDTO.setSku(producto.getSku());
//
 //        ProductoDTO productoDTO2 = new ProductoDTO(producto.getNombre(), producto.getPrecio(), producto.getSku());
//
//        return Optional.ofNullable(productoDTO2);

//        return Optional.ofNullable(producto)
//                .map(p -> new ProductoDTO(producto.getNombre(), producto.getPrecio(), producto.getSku()));

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
