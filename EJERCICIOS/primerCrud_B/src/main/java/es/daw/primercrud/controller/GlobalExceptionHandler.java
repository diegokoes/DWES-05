package es.daw.primercrud.controller;


import es.daw.primercrud.dto.ErrorDTO;
import es.daw.primercrud.dto.ErrorDetailDTO;
import jakarta.validation.ValidationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.HashMap;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {

    @ExceptionHandler(NumberFormatException.class)
    public ResponseEntity<ErrorDTO> handleNumberFormatException(NumberFormatException ex) {
        ErrorDTO error = new ErrorDTO(
                "Invalid number format",
                ex.getMessage()
        );
        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
    }

    // VERSIÓN 1, LOS DETALLES DE VALIDACIÓN EN STRING
//    @ExceptionHandler(MethodArgumentNotValidException.class)
//    public ResponseEntity<ErrorDTO> hadlerValidationException(MethodArgumentNotValidException ex) {
//
//        Map<String,String> errorMessages = new HashMap<>();
//
//        ex.getBindingResult().getAllErrors().forEach((error) -> {
//            String fieldName = ((FieldError) error).getField();
//            System.out.println("* field: " + fieldName );
//            String defaultMessage = error.getDefaultMessage();
//            System.out.println("* defaultMessage: " + defaultMessage );
//            errorMessages.put(fieldName, defaultMessage);
//        });
//
//        ErrorDTO error = new ErrorDTO("Error al validar el JSON",
//                //ex.getMessage()
//                errorMessages.toString()
//        );
//        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);
//
//    }

    // VERSIÓN 1, LOS DETALLES DE VALIDACIÓN EN JSON COMO OBJETO
    @ExceptionHandler(MethodArgumentNotValidException.class)
    public ResponseEntity<ErrorDetailDTO> hadlerValidationException(MethodArgumentNotValidException ex) {

        Map<String,String> errorMessages = new HashMap<>();

        /*
            getBindingResult(): Obtiene el objeto de tipo BindingResult, que contiene detalles sobre los errores de validación.
            getAllErrors(): Devuelve una lista de objetos de tipo ObjectError que representan los errores encontrados.
         */
        ex.getBindingResult().getAllErrors().forEach((error) -> {
            String fieldName = ((FieldError) error).getField();
            System.out.println("* field: " + fieldName );
            String defaultMessage = error.getDefaultMessage();
            System.out.println("* defaultMessage: " + defaultMessage );
            errorMessages.put(fieldName, defaultMessage);
        });

        ErrorDetailDTO error = new ErrorDetailDTO(
                "Error al validar el JSON del body",
                errorMessages
        );

        return new ResponseEntity<>(error, HttpStatus.BAD_REQUEST);

    }

}
