package es.daw.actividad.service;

import es.daw.actividad.dto.PedidoRequestDTO;
import es.daw.actividad.entity.Pedido;
import es.daw.actividad.entity.Plato;
import es.daw.actividad.repository.PedidoRepository;
import es.daw.actividad.repository.PlatoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class PedidoService {

    private final PedidoRepository pedidoRepository;
    private final PlatoRepository platoRepository;

    @Autowired
    public PedidoService(PedidoRepository pedidoRepository, PlatoRepository platoRepository) {
        this.pedidoRepository = pedidoRepository;
        this.platoRepository = platoRepository;
    }

    /**
     *
     * @return
     */
    public List<Pedido> obtenerPedidos() {
        return pedidoRepository.findAll();
    }

    /**
     *
     * @param pedidoDTO
     * @return
     */
    public Pedido crearPedido(PedidoRequestDTO pedidoDTO) {
        // Convertir los IDs de los platos en entidades Plato
        List<Plato> platos = platoRepository.findAllById(pedidoDTO.getListaPlatos());

        // Crear un nuevo Pedido
        Pedido nuevoPedido = new Pedido();
        nuevoPedido.setCliente(pedidoDTO.getCliente());
        nuevoPedido.setFecha(pedidoDTO.getFecha());
        nuevoPedido.setTotal(pedidoDTO.getTotal());
        for (Plato plato : platos) {
            nuevoPedido.addPlato(plato);
        }
        //nuevoPedido.setListaPlatos(platos);

        return pedidoRepository.save(nuevoPedido);

    }

    /**
     *
     * @param fechaInicio
     * @param fechaFin
     * @return
     */
    public List<Pedido> obtenerPedidosPorFecha(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return pedidoRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
}
