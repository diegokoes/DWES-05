package es.daw.api.estudiantes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="cursos")
@AllArgsConstructor
//@Getter
//@Setter
@Data
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
            joinColumns = @JoinColumn(name = "curso_id"),
            inverseJoinColumns = @JoinColumn(name = "estudiante_id")
    )
    private List<Estudiante> estudiantesInscritos;


    public Curso(){
        estudiantesInscritos = new ArrayList<>();
    }

    // ---------- bidireccional --------
    public void addEstudiante(Estudiante estudiante){
        estudiantesInscritos.add(estudiante);
        //estudiante.addCurso(this);
        estudiante.getCursos().add(this);
    }

    public void removeEstudiante(Estudiante estudiante){
        estudiantesInscritos.remove(estudiante);
        //estudiante.removeCurso(this); //infinito
        estudiante.getCursos().remove(this);
    }


    @Override
    public String toString() {
        return "Curso{" +
                "id=" + id +
                ", nombre='" + nombre + '\'' +
                ", descripcion='" + descripcion + '\'' +
                ", codigo='" + codigo + '\'' +
                '}';
    }
}
