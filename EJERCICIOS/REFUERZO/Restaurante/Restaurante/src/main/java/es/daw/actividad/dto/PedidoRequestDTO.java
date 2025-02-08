package es.daw.actividad.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class PedidoRequestDTO {
    private String cliente;
    private LocalDateTime fecha;
    private double total;
    private List<Long> listaPlatos;  // Aquí solo recibirás los IDs de los platos

}

