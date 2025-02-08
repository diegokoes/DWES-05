package es.daw.actividad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="platos")
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"pedidos"})
public class Plato {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false)
    private double precio;

    @Column(nullable = false)
    private String categoria;

    @ManyToMany(mappedBy = "listaPlatos")
    private List<Pedido> pedidos;

    public Plato(){
        pedidos = new ArrayList<>();
    }

    public void addPedido(Pedido pedido) {
        pedidos.add(pedido);
        pedido.getListaPlatos().add(this);
    }

    public void removePedido(Pedido pedido) {
        pedidos.remove(pedido);
        pedido.getListaPlatos().remove(this);
    }

    @Override
    public String toString() {
        return "Plato{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", precio=" + precio +
                ", categoria='" + categoria + '\'' +
                '}';
    }
}
