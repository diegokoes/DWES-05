package es.daw.primermvc.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString


/*
No es necesario implementar la interface Serializable. Si es obligatorio:
- Si planeas almacenar el objeto en una sesión de usuario (HTTP session).
- Si necesitas enviar el objeto a través de un flujo de datos (como RMI o mensajes JMS).
- Para otros casos de serialización binaria en Java.

 */
public class ProductoDTO implements Serializable {
    @NotBlank(message = "El nombre es obligatorio")
    private String nombre;
    @NotBlank(message = "El SKU es obligatorio")
    @Size(min = 4, max = 4, message = "El SKU debe tener exactamente 4 caracteres")
    private String sku;
    @Min(value = 100, message = "El precio debe superior a 99")
    private int precio;
}
