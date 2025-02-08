package es.daw.actividad.controller;

import es.daw.actividad.dto.PedidoRequestDTO;
import es.daw.actividad.entity.Pedido;
import es.daw.actividad.service.PedidoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/pedidos")
public class PedidoController {

    private final PedidoService pedidoService;

    @Autowired
    public PedidoController(PedidoService pedidoService) {
        this.pedidoService = pedidoService;
    }

    @GetMapping
    public ResponseEntity<List<Pedido>> obtenerPedidos() {
        List<Pedido> pedidos = pedidoService.obtenerPedidos();

        return new ResponseEntity<>(pedidos, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Void> crearPedido(@RequestBody PedidoRequestDTO pedidoDTO) {
        pedidoService.crearPedido(pedidoDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Pedido>> obtenerPedidosPorFecha(@PathVariable LocalDate fecha) {
        LocalDateTime startOfDay = fecha.atStartOfDay(); // 00:00 del día
        LocalDateTime endOfDay = fecha.atTime(LocalTime.MAX); // 23:59:59 del día

        List<Pedido> pedidos = pedidoService.obtenerPedidosPorFecha(startOfDay, endOfDay);

        if (pedidos.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(pedidos);
    }
}
