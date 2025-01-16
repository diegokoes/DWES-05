package es.daw.primercrud.controller;

import es.daw.primercrud.config.DawConfig;
import es.daw.primercrud.config.DawConfig;
import es.daw.primercrud.dto.DawResponseDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class DawController {

    @Autowired
    private DawConfig dawConfig;

    //private final DawConfig dawConfig;
    //@Autowired
//    public DawController(DawConfig dawConfig) {
//        this.dawConfig = dawConfig;
//    }

    @GetMapping("/values-conf")
    public DawResponseDTO values() {
        return new DawResponseDTO(dawConfig.getCode(), dawConfig.getMessage());
    }

}
