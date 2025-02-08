package es.daw.actividad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class CompraDTO {
    private String cliente;
    private LocalDateTime fecha;
    private double total;
    private List<Long> listaProductos;
}
