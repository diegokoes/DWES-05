package es.daw.primercrud.dto;

import lombok.*;

import java.util.Map;

/*
No es necesario implementar la interface Serializable. Si es obligatorio:
- Si planeas almacenar el objeto en una sesión de usuario (HTTP session).
- Si necesitas enviar el objeto a través de un flujo de datos (como RMI o mensajes JMS).
- Para otros casos de serialización binaria en Java.

 */
// Si tu clase ErrorDetailDTO no tiene constructores, getters o setters,
// Jackson no podrá serializarla correctamente
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@ToString
public class ErrorDetailDTO {
    private String message;
    private Map<String,String> details;
}
