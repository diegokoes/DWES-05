package es.daw.api.eventos.controller;

import es.daw.api.eventos.dto.EventoDTO;
import es.daw.api.eventos.entity.Evento;
import es.daw.api.eventos.entity.Organizador;
import es.daw.api.eventos.exception.NotFoundOrganizerException;
import es.daw.api.eventos.repository.EventoRepository;
import es.daw.api.eventos.repository.OrganizadorRepository;
import jakarta.validation.Valid;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/api/events")
public class EventoController {

    private final EventoRepository eventoRepository;
    private final OrganizadorRepository organizadorRepository;

    public EventoController(EventoRepository eventoRepository, OrganizadorRepository organizadorRepository) {
        this.eventoRepository = eventoRepository;
        this.organizadorRepository = organizadorRepository;
    }

    @GetMapping("/list")
    public Page<EventoDTO> findAllDTO(Pageable pageable) {
        Page<Evento> eventosPage = eventoRepository.findAll(pageable);

        Page<EventoDTO> eventosDTOPage = eventosPage.map(p -> {
            EventoDTO eventoDTO = new EventoDTO();
            eventoDTO.setCodigo(p.getCodigo());
            eventoDTO.setNombre(p.getNombre());
            eventoDTO.setLugar(p.getLugar());
            eventoDTO.setFecha_publicacion(p.getFecha());
            eventoDTO.setPrecio(p.getPrecio());
            eventoDTO.setOrganizadorCodigo(p.getOrganizador().getCodigo());

            return eventoDTO;
        });

        return eventosDTOPage;
    }


    @PostMapping
    public ResponseEntity<EventoDTO> crearEvento(@Valid @RequestBody EventoDTO eventoDTO) {
        Optional<Organizador> organizadorOpt = organizadorRepository.findByCodigo(eventoDTO.getOrganizadorCodigo());

        if (organizadorOpt.isEmpty()) {
            //return ResponseEntity.badRequest().body("El organizador con código "+eventoDTO.getOrganizadorCodigo()+" no existe"); // Error si el organizador no existe
            throw new NotFoundOrganizerException("El organizador con código "+eventoDTO.getOrganizadorCodigo()+" no existe");
        }

        Evento evento = new Evento();
        evento.setCodigo(eventoDTO.getCodigo());
        evento.setNombre(eventoDTO.getNombre());
        evento.setLugar(eventoDTO.getLugar());
        evento.setFecha(eventoDTO.getFecha_publicacion());
        evento.setPrecio(eventoDTO.getPrecio());
        evento.setOrganizador(organizadorOpt.get());

        Evento eventoGuardado = eventoRepository.save(evento);

        EventoDTO out = new EventoDTO();
        out.setCodigo(eventoGuardado.getCodigo());
        out.setNombre(eventoGuardado.getNombre());


        return new ResponseEntity<>(out, HttpStatus.CREATED);
    }


}
