package es.daw.primermvc.controller;

import es.daw.primermvc.dto.ProductoDTO;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;

@Controller
@RequestMapping("/productos")
//@RequiredArgsConstructor
//@SessionAttributes("productos") // Permite que ciertos datos del modelo se almacenen en la sesión de usuario
public class ProductoController {

    // ---------------------
    //private final String BASE_URL = "http://localhost:8080/productos"; // URL del API REST

    @Value("${daw.api.url}")
    public String BASE_URL;
    // --------------------

    // ---------------------------
    /*
     * RestTemplate: Es una clase de Spring que se usa para realizar solicitudes HTTP (como GET, POST, PUT, DELETE) a servicios web externos.
     * Aquí la usamos para interactuar con la API REST que ya tienes configurada en el puerto 8080.
     */
//    @Autowired
//    private RestTemplate restTemplate;

//    private final RestTemplate restTemplate;
//
//    @Autowired
//    public ProductoController(RestTemplate restTemplate) {
//        this.restTemplate = restTemplate;
//    }
    // ---------------------------

    // ---------- INYECTANDO webClient por constructor -------------------
    private final WebClient webClient; // una vez inicializado no lo voy a tocar. Al inyectarlo como bean es un singleton
    @Autowired
    public ProductoController(WebClient webClient) {
        this.webClient = webClient;
    }
    // ---------------------------------

    // Model: es un objeto proporcionado por Spring MVC que se utiliza para pasar datos desde el controlador a la vista
    // (plantilla Thymeleaf).
    // En este caso usamos model para pasar la lista de productos obtenida de la API REST a la plantilla lista.html
    @GetMapping
    public String listarProductos(Model model) {
        System.out.println("***************** LISTAR PRODUCTOS ***************");
        // Realiza una solicitud GET al API REST
        // Ojo!!! el objeto producto debe tener la misma estructura entre el API REST (entity) y la app MVC
        //List<ProductoDTO> productos = restTemplate.getForObject(BASE_URL, List.class);

        List<ProductoDTO> productos = webClient.get()
                        .uri(BASE_URL+"/list-dto") // url del api
                        .retrieve() // Envía la solicitud y espera la respuesta
                        .bodyToMono(new ParameterizedTypeReference<List<ProductoDTO>>(){})
                        .block(); // Bloquea para que sea síncrono

        System.out.println("**** productos:"+productos);

        model.addAttribute("productos", productos);

        System.out.println(" ******** REDIRIGIENDO A LISTA TH");
        return "lista";
    }

    // -----------------------------------------------------
    // PENDIENTE: listarProductos pero controlando cualquier error al conectar al API Rest
    // Si hay error, redirigimos a error.html con el mensaje

    @GetMapping("/nuevo")
    public String mostrarFormularioNuevo(Model model) {
        // Paso al modelo un objeto vacío...
        model.addAttribute("producto", new ProductoDTO());

        return "formulario";

    }

    //@PostMapping("/guardar")
    // Guardar el producto (llamamos al API rest adecuadamente)

    //@GetMapping("/editar/{id}")
    // Mostrar el formulario de edición de productos

    @GetMapping("/eliminar/{sku}")
    // Si redirigimos a plantilla html, necesito el model para pasarle datos
    // Si redirigimos a una url, no necesito model y uso en el return redirect:
    //public String eliminarProducto(@PathVariable("sku") String sku, Model model) {
    public String eliminarProducto(@PathVariable("sku") String sku) {

        webClient.delete()
                .uri(BASE_URL+"/delete-sku/"+sku)
                .retrieve()
                .bodyToMono(Void.class)
                .block();

//        // Si quiero redirigir a una vista
//        model.addAttribute("mensaje", "mensaje a enviar a la vista");
//        return "lista";

        return "redirect:/productos";
    }

    // -----------------------------------------------------
    @PostMapping("/guardar")
    // BindingResult captura cualquier error de validación
    public String guardarProducto(@Valid @ModelAttribute ProductoDTO producto, BindingResult result, Model model) {

        if (result.hasErrors()) {
            System.out.println("****** HAY ERRORES *********");
            result.getAllErrors().forEach(error -> System.out.println(error.toString()));
            System.out.println("****************************");

            model.addAttribute("producto", producto);
            return "formulario";

        }

        webClient.post()
                .uri(BASE_URL+"/add-dto")
                .bodyValue(producto)
                .retrieve()
                .bodyToMono(Void.class) // No quiero procesar la respuesta
                .block();
        return "redirect:/productos";

    }

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

    // endpoint que se invoca desde el formulario (action = /productos/actualizar)
    @PostMapping("/actualizar")
    public String actualizarProducto(@ModelAttribute ProductoDTO producto) {

            webClient.put()
                    .uri(BASE_URL+"/update-sku/"+producto.getSku())
                    .bodyValue(producto)
                    .retrieve()
                    .bodyToMono(Void.class)
                    .block();

            return "redirect:/productos"; // se pinte de nuevo la lista de productos...


    }

}
