package es.daw.springsecurity.model;

import java.util.List;

public class EventoResponse {
    private List<Evento> content;
    private int totalPages;
    private int number;

    public List<Evento> getContent() {
        return content;
    }

    public int getTotalPages() {
        return totalPages;
    }

    public int getNumber() {
        return number;
    }
}

