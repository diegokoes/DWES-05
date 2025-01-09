package es.daw.primercrud.dto;

import lombok.*;

import java.io.Serializable;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@ToString
public class ProductoDTO implements Serializable {
    private String nombre;
    private String sku;
    private int precio;
}
