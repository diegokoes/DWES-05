package es.daw.api.gestionexcepciones.dto;

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
public class ErrorDTO {
    private String message;
    private String code;
    private Map<String,String> details;

    public ErrorDTO(String message, String code, Map<String, String> details) {
        this.message = message;
        this.code = code;
        this.details = details;
    }

    public String getMessage() {
        return message;
    }

    public String getCode() {
        return code;
    }

    public Map<String, String> getDetails() {
        return details;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public void setDetails(Map<String, String> details) {
        this.details = details;
    }

    @Override
    public String toString() {
        return "ErrorDTO{" +
                "message='" + message + '\'' +
                ", code='" + code + '\'' +
                ", details=" + details +
                '}';
    }
}

