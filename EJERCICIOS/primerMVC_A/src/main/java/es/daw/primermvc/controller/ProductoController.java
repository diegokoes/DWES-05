package es.daw.primermvc.controller;

import es.daw.primermvc.dto.ProductoDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
@RequestMapping("/productos")
//@SessionAttributes("productos") // Permite que ciertos datos del modelo se almacen en la sesión de usuario
public class ProductoController {

    // CANDIDATO A PASARLO A .... MEJORAR... conf.propierties
    //private final String BASE_URL = "http://localhost:8080/productos";

    // CARGANDO DE application.properties
    @Value("${daw.api.url}")
    private String BASE_URL;

//    // ----------- INYECCIÓN POR CAMPO -------------
//    //@Autowired
//    private RestTemplate restTemplate;
//    // ------------------------
//    // ----------- INYECCIÓN POR CONSTRUCTOR ---------
//    @Autowired
//    public ProductoController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
//    //--------------------------------

    private final WebClient webClient;

    @Autowired
    public ProductoController(WebClient webClient) {
        this.webClient = webClient;
    }

    // Model: es un objeto proporcionado por Spring MVC que se utiliza para pasar datos
    //desde el controlador a la vista (plantilla Thymeleaf)
    @GetMapping
    public String listarProductos(Model model) {
        List<ProductoDTO> productos; //pendiente llamar a la URL del api rest....

        // uso del resttemplate para conectarme al api rest de productos...
        productos = webClient.get()
                .uri(BASE_URL) // url del api (devuelve productos no dto ????)
                .retrieve() //Enviar la solicitud y espera la respuesta
                .bodyToMono(new ParameterizedTypeReference<List<ProductoDTO>>() {})
                .block(); //bloquea para que se síncrono

        model.addAttribute("productos",productos);

        return "list";
    }

    // ------------ NUEVO PRODUCTO ---------------
    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {

        model.addAttribute("producto", new ProductoDTO());

        return "formulario";
    }

    @PostMapping("/guardar")
    public String guardarProducto(@ModelAttribute ProductoDTO producto) {
        webClient.post()
                .uri(BASE_URL+"/add-dto")
                .bodyValue(producto)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return "redirect:/productos";


        //return "list";
        //return "/productos"; // no funciona!!!Error resolving template [/productos]
        // return  .... no devolver nada o "" y antes llamar al método....

        //listarProductos(model); //ohhhhhhhhhh no rula
        //return "";
    }

    // -----------------------


    // --------- borrar ---------
    @GetMapping("/eliminar/{sku}")
    public String eliminarProducto(@PathVariable("sku") String sku) {
        webClient.delete()
                .uri(BASE_URL+"/delete-sku/"+sku)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

        return "redirect:/productos";
    }


    // --------- modificar un producto --------
    @GetMapping("/editar/{sku}")
    public String mostrarFormularioEditar(@PathVariable("sku") String sku, Model model) {

        ProductoDTO producto = webClient.get()
                .uri(BASE_URL+"/find-sku/"+sku)
                .retrieve()
                .bodyToMono(ProductoDTO.class)
                .block();


        model.addAttribute("producto", producto);

        return "formulario";

    }
    
}
