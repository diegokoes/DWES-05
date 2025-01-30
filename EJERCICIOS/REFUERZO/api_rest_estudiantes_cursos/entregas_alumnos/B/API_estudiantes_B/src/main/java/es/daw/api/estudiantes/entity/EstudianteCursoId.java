package es.daw.api.estudiantes.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

@Embeddable
@Data
@NoArgsConstructor
@AllArgsConstructor
public class EstudianteCursoId implements Serializable {

    private Long estudianteId;
    private Long cursoId;

}
