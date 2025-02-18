package es.daw.api.eventos.dto;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;
import org.springframework.validation.annotation.Validated;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class EventoDTO {

    @Pattern(regexp = "^EV\\d{3}$", message = "{evento.codigo.pattern}")
    private String codigo;

    @Size(max = 25, message = "{evento.nombre.size}")
    @NotBlank(message = "{evento.nombre.blank}")
    private String nombre;

    @NotBlank(message = "{evento.lugar.blank}")
    private String lugar;

    @NotNull(message = "{evento.fecha.notnull}")
    private LocalDateTime fecha_publicacion;

    @DecimalMax(value = "200.00", message = "{evento.precio.max}")
    @NotNull(message = "{evento.precio.notnull}")
    private BigDecimal precio;

    @NotBlank(message = "{evento.organizador.notblank}")
    private String organizadorCodigo;

    // Getters y Setters
}
