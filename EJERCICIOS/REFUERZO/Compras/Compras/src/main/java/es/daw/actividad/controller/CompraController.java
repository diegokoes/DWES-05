package es.daw.actividad.controller;

import es.daw.actividad.dto.CompraDTO;
import es.daw.actividad.entity.Compra;
import es.daw.actividad.service.CompraService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;

@RestController
@RequestMapping("/compras")
public class CompraController {

    private final CompraService compraService;

    @Autowired
    public CompraController(CompraService compraService) {
        this.compraService = compraService;
    }

    @GetMapping
    public ResponseEntity<List<Compra>> obtenerCompras() {
        List<Compra> compras = compraService.obtenerCompras();

        return new ResponseEntity<>(compras, HttpStatus.OK);

    }

    @PostMapping
    public ResponseEntity<Void> comprar(@RequestBody CompraDTO compraDTO) {
        compraService.guardarCompra(compraDTO);

        return ResponseEntity.status(HttpStatus.CREATED).body(null);
    }

    @GetMapping("/fecha/{fecha}")
    public ResponseEntity<List<Compra>> obtenerComprasPorFecha(@PathVariable LocalDate fecha) {

        LocalDateTime startOfDay = fecha.atStartOfDay(); // 00:00 del día
        LocalDateTime endOfDay = fecha.atTime(LocalTime.MAX); // 23:59:59 del día

        List<Compra> compras = compraService.obtenerComprasPorFecha(startOfDay, endOfDay);

        if (compras.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
        return ResponseEntity.status(HttpStatus.OK).body(compras);
    }

}
