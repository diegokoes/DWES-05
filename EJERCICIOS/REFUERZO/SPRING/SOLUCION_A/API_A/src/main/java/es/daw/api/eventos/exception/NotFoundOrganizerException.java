package es.daw.api.eventos.exception;

public class NotFoundOrganizerException extends RuntimeException{
    public NotFoundOrganizerException(String msg) {
        super(msg);
    }
}
