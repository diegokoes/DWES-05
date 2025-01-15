package es.daw.primercrud.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data // Genera getters, setters, equals, constructor con solo campos requeridos, hashCode y toString automáticamente.
@AllArgsConstructor // Genera un constructor con todos los campos.
@NoArgsConstructor // Genera un constructor vacío.
public class DawResponseDTO {
    private String code;
    private String message;
}