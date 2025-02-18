package es.daw.springsecurity.service;

import es.daw.springsecurity.model.Evento;
import es.daw.springsecurity.model.EventoResponse;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

@Service
public class EventoService {

    private final WebClient webClientEventos;

    public EventoService(@Qualifier("webClientEventos") WebClient webClientEventos) {
        this.webClientEventos = webClientEventos;
    }

    public EventoResponse obtenerTodosLosEventos(int page, int size) {
        String url = String.format("?page=%d&size=%d", page, size);
        return webClientEventos.get()
                .uri("/list"+url)
                .retrieve()
                //.bodyToMono(new ParameterizedTypeReference<List<Evento>>() {})
                .bodyToMono(EventoResponse.class)
                .block();
    }

    public void guardarEvento(Evento evento, String token) {
        webClientEventos.post()
                //.uri("/")
                .header("Authorization", token)
                .bodyValue(evento)
                .retrieve()
                .bodyToMono(Void.class)
                .block();
    }
}
