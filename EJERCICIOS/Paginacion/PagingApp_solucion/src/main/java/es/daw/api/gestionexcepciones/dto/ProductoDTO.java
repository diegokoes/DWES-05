package es.daw.api.gestionexcepciones.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class ProductoDTO implements Serializable {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El SKU es obligatorio")
    @Size(min = 4, max = 4, message = "El SKU debe tener exactamente 4 caracteres")
    private String sku;
    @Min(value = 100, message = "El precio debe superior a 99")
    //private int precio;
    private Integer precio;
}