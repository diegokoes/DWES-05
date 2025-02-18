package es.daw.api.eventos.entity;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="organizadores")
@Getter
@Setter
@AllArgsConstructor
@JsonIgnoreProperties("eventos") //QUITAR A ALUMNOS // Evita que Spring Data Rest genere bucles en JSON
public class Organizador {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String codigo;

    @Column(nullable = false, unique = true)
    private String nombre;

    @Column(nullable = false, length = 500)
    private String descripcion;

    @Column(nullable = false)
    private String paisOrigen;

    @OneToMany(mappedBy = "organizador", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Evento> eventos;

    public Organizador() {
        eventos = new ArrayList<>();
    }


}
