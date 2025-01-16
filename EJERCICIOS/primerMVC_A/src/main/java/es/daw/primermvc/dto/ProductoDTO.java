package es.daw.primermvc.dto;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
public class ProductoDTO {
    private String nombre;
    private String sku;
    private int precio;
}
