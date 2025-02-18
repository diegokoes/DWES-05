package es.daw.springsecurity.model;

import com.fasterxml.jackson.annotation.JsonInclude;
import jakarta.validation.constraints.*;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Evento {

    @Pattern(regexp = "^EV\\d{3}$", message = "El código debe comenzar con 'EV' seguido de tres dígitos (Ejemplo: EV132)")
    private String codigo;

    @NotBlank(message = "El nombre no puede estar vacío")
    @Size(max = 25, message = "El nombre no puede tener más de 25 caracteres")
    private String nombre;

    @NotBlank(message = "El lugar no puede estar vacío")
    private String lugar;

    private LocalDateTime fecha_publicacion;

    @NotNull(message = "El precio es obligatorio")
    @DecimalMax(value = "200.00", message = "El precio no puede ser mayor de 200 euros")
    private BigDecimal precio;

    private String organizadorCodigo;

}

