package es.daw.actividad.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="pedidos")
@Data
@AllArgsConstructor
@JsonIgnoreProperties({"listaPlatos"})
public class Pedido {
    @Id @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false)
    private String cliente;

    @Column(nullable = false)
    private LocalDateTime fecha;

    @Column(nullable = false)
    private double total;

    @ManyToMany
    @JoinTable(
            name = "pedidos_platos",
            joinColumns = @JoinColumn(name = "pedido_id"),
            inverseJoinColumns = @JoinColumn(name = "plato_id")
    )
    private List<Plato> listaPlatos;

    public Pedido(){
        listaPlatos = new ArrayList<>();
    }

    public void addPlato(Plato plato) {
        this.listaPlatos.add(plato);
        plato.getPedidos().add(this);
    }

    public void removePlato(Plato plato) {
        this.listaPlatos.remove(plato);
        plato.getPedidos().remove(this);
    }

    @Override
    public String toString() {
        return "Pedido{" +
                "id=" + id +
                ", cliente='" + cliente + '\'' +
                ", fecha=" + fecha +
                ", total=" + total +
                '}';
    }
}
