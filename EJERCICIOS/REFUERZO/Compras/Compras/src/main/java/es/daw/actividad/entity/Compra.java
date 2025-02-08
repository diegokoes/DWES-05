package es.daw.actividad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="compras")
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"listaProductos"})
public class Compra {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private double total;

    @ManyToMany
    @JoinTable(
            name = "compra_producto",
            joinColumns = @JoinColumn(name = "compra_id"),
            inverseJoinColumns = @JoinColumn(name = "producto_id")
    )
    private List<Producto> listaProductos;

    public Compra() {
        listaProductos = new ArrayList<>();
    }

    public void addProducto(Producto producto) {
        listaProductos.add(producto);
        producto.getCompras().add(this);
    }

    public void removeProducto(Producto producto) {
        listaProductos.remove(producto);
        producto.getCompras().remove(this);
    }

}

