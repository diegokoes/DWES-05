package es.daw.api.estudiantes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cursos")
@Data
@AllArgsConstructor
public class Curso {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;
    private String descripcion;
    private String codigo;

    @ManyToMany
    @JoinTable(
            name = "estudiantes_cursos",
            joinColumns = @JoinColumn(name ="curso_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante> estudiantesInscritos;

    public Curso() {
        estudiantesInscritos = new ArrayList<>();
    }

    public void addEstudiante(Estudiante estudiante) {
        estudiantesInscritos.add(estudiante);
        estudiante.getCursos().add(this);
    }

    public void removeEstudiante(Estudiante estudiante) {
        estudiantesInscritos.remove(estudiante);
        estudiante.getCursos().remove(this);
    }

}
