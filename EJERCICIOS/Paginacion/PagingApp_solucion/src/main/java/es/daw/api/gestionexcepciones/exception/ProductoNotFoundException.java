package es.daw.api.gestionexcepciones.exception;

public class ProductoNotFoundException extends RuntimeException {
    //private static final long serialVersionUID = 1L;

    public ProductoNotFoundException(String sku) {
        super("El producto " + sku + " no existe.");
    }
}
