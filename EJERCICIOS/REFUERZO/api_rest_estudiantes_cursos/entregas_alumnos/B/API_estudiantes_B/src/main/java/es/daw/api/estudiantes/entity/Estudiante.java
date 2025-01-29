package es.daw.api.estudiantes.entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="estudiantes")
@AllArgsConstructor
//@Getter
//@Setter
@Data
public class Estudiante {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nia;

    private String nombre;

    private String email;

    @ManyToMany(mappedBy = "estudiantesInscritos")
    private List<Curso> cursos;

    public Estudiante(){
        cursos = new ArrayList<>();
    }


    // ---- bidreccional --
    public void addCurso(Curso curso){
        cursos.add(curso);
        //curso.addEstudiante(this); //infinito
        curso.getEstudiantesInscritos().add(this);
    }

    public void removeCurso(Curso curso){
        cursos.remove(curso);
        //curso.removeEstudiante(this);
        curso.getEstudiantesInscritos().remove(this);
    }

    @Override
    public String toString() {
        return "Estudiante{" +
                "id=" + id +
                ", nia='" + nia + '\'' +
                ", nombre='" + nombre + '\'' +
                ", email='" + email + '\'' +
                '}';
    }
}

