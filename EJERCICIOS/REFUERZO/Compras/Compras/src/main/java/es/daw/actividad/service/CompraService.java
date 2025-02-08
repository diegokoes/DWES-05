package es.daw.actividad.service;

import es.daw.actividad.dto.CompraDTO;
import es.daw.actividad.entity.Compra;
import es.daw.actividad.entity.Producto;
import es.daw.actividad.repository.CompraRepository;
import es.daw.actividad.repository.ProductoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class CompraService {

    private final CompraRepository compraRepository;
    private final ProductoRepository productoRepository;

    @Autowired
    public CompraService(CompraRepository compraRepository, ProductoRepository productoRepository) {
        this.compraRepository = compraRepository;
        this.productoRepository = productoRepository;
    }

    /**
     *
     * @return
     */
    public List<Compra> obtenerCompras(){
        return compraRepository.findAll();
    }


    /**
     *
     * @param compraDTO
     * @return
     */
    public Compra guardarCompra(CompraDTO compraDTO){
        List<Producto> productos = productoRepository.findAllById(compraDTO.getListaProductos());

        Compra compra = new Compra();
        compra.setCliente(compraDTO.getCliente());
        compra.setFecha(compraDTO.getFecha());
        compra.setTotal(compraDTO.getTotal());
        for(Producto producto: productos){
            compra.addProducto(producto);
        }
        return compraRepository.save(compra);

    }

    public List<Compra> obtenerComprasPorFecha(LocalDateTime fechaInicial, LocalDateTime fechaFinal){
        return compraRepository.findByFechaBetween(fechaInicial,fechaFinal);
    }
}
