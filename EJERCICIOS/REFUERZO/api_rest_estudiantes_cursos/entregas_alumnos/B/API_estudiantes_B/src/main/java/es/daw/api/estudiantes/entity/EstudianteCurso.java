package es.daw.api.estudiantes.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Table(name = "estudiantes_cursos")
@Data
public class EstudianteCurso {

    @EmbeddedId
    private EstudianteCursoId id;

    @ManyToOne
    @MapsId("estudianteId")
    @JoinColumn(name = "estudiante_id")
    private Estudiante estudiante;

    @ManyToOne
    @MapsId("cursoId")
    @JoinColumn(name = "curso_id")
    private Curso curso;

    @Column(name = "fecha_inscripcion", nullable = false)
    private LocalDate fechaInscripcion;

    public EstudianteCurso(Estudiante estudiante, Curso curso) {
        this.curso = curso;
        this.estudiante = estudiante;
        this.id = new EstudianteCursoId(estudiante.getId(), curso.getId());
    }

    public EstudianteCurso() {
    }

    @PrePersist
    protected void onCreate() {
        fechaInscripcion = LocalDate.now();
    }




}