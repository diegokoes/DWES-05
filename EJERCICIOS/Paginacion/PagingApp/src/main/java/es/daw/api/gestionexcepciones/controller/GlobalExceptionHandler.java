package es.daw.api.gestionexcepciones.controller;

import es.daw.api.gestionexcepciones.dto.ErrorDTO;
import es.daw.api.gestionexcepciones.exception.ProductoNotFoundException;
import jakarta.validation.ConstraintViolationException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;
import java.util.Map;

@RestControllerAdvice
public class GlobalExceptionHandler {

    private static final Logger logger = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDTO> hadlerValidationException(MethodArgumentNotValidException ex) {

        Map<String, String> errorMessages = new HashMap<>();

        /*
            getBindingResult(): Obtiene el objeto de tipo BindingResult, que contiene detalles sobre los errores de validación.
            getAllErrors(): Devuelve una lista de objetos de tipo ObjectError que representan los errores encontrados.
         */
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            System.out.println("* field: " + fieldName);
            String defaultMessage = error.getDefaultMessage();
            System.out.println("* defaultMessage: " + defaultMessage);
            errorMessages.put(fieldName, defaultMessage);
        });


        ErrorDTO error = new ErrorDTO(
                "Error al validar el JSON del body",
                HttpStatus.BAD_REQUEST.toString(),
                errorMessages
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

    // Podríamos tener el método en el controlador directamente...
    @ExceptionHandler(ProductoNotFoundException.class)
    public ResponseEntity<ErrorDTO> handleProductoNotFoundException(ProductoNotFoundException ex) {

        logger.info("Se ha capturado una excepción ProductoNotFoundException.");
        logger.debug("Detalles de la excepción: {}", ex.getMessage());


        Map<String, String> errorMessages = new HashMap<>();
        errorMessages.put("error", ex.getMessage());

        ErrorDTO error = new ErrorDTO(
                "Error paginando Productos ",
                HttpStatus.NOT_FOUND.toString(),
                errorMessages
        );

        logger.trace("ErrorDTO generado: {}", error);

        return new ResponseEntity<>(error, HttpStatus.NOT_FOUND);
    }



}

