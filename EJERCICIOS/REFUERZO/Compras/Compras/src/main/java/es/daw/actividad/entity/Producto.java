package es.daw.actividad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="productos")
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"compras"})
public class Producto {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private String categoria;

    @ManyToMany(mappedBy = "listaProductos")
    private List<Compra> compras;

    public Producto(){
        compras = new ArrayList<>();
    }

    public void addCompra(Compra compra){
        compras.add(compra);
        compra.getListaProductos().add(this);
    }
    public void removeCompra(Compra compra){
        compras.remove(compra);
        compra.getListaProductos().remove(this);
    }
}


