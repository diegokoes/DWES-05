package es.daw.api.estudiantes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name="estudiantes")
@Data
@AllArgsConstructor
public class Estudiante {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nia;
    private String nombre;
    private String email;

    @ManyToMany(mappedBy = "estudiantesInscritos")
    private List<Curso> cursos;

    public Estudiante() {
        cursos = new ArrayList<>();
    }

    // ------- bidireccional -----
    public void addCurso(Curso curso) {
        cursos.add(curso);
        // curso.addEstudiante(this); // bucle infinito
        curso.getEstudiantesInscritos().add(this);
    }

    public void removeCurso(Curso curso) {
        cursos.remove(curso);
        //curso.removeEstudiante(this); // bucle infinito
        curso.getEstudiantesInscritos().remove(this);
    }


}
