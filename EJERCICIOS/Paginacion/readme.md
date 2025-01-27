# Paginar resultados con Spring. Práctica guiada

Para paginar los resultados en Spring, el enfoque más común es usar Pageable junto con la interfaz PagingAndSortingRepository o JpaRepository de Spring Data. 

Esto te permitirá devolver un subconjunto de los productos con un Page en lugar de cargar todos los productos a la vez.


## Pasos para implementar la paginación

1. **Modificar el Repositorio para Paginación:** Primero, asegúrate de que tu repositorio productoRepository extienda **JpaRepository** o **PagingAndSortingRepository**, que ya tiene métodos de paginación como findAll(Pageable pageable).

2. **Modificar el Endpoint para Aceptar Parámetros de Paginación:** Tendrás que modificar el endpoint para aceptar parámetros como page y size, que permitirán a los clientes controlar la paginación.

3. **Devolver un Page de DTOs:** En lugar de devolver una lista, devolverás un objeto Page<ProductoDTO>, que tiene información sobre la página actual, el tamaño y los elementos de esa página.

___

## Incluye en tu proyecto este endpoint que permite paginación

```
@GetMapping("/list-dto")
public Page<ProductoDTO> findAllDTO(Pageable pageable) {
    // Recuperamos una página de productos con la paginación
    Page<Producto> productosPage = productoRepository.findAll(pageable);

    // Convertimos la página de productos a una página de DTOs
    Page<ProductoDTO> productosDTOPage = productosPage.map(p -> productoService.convert2ProductoDTO(p).get());

    return productosDTOPage;
}

```

### Pageable:

El parámetro Pageable es proporcionado automáticamente por Spring Data. 

Los usuarios pueden hacer peticiones como ?page=0&size=10 para indicar que quieren la primera página con 10 elementos por página.

### Page<Producto>:

findAll(pageable) devuelve un objeto Page que contiene una página de entidades Producto. 

Este objeto también contiene metadatos sobre la paginación, como el número total de elementos y páginas.


### map:

Usamos map para transformar cada entidad Producto a su correspondiente ProductoDTO. 

Esto permite seguir manteniendo la lógica de conversión de tus objetos.


### Page<ProductoDTO>:

La respuesta es una página de DTOs. 

Spring Data manejará automáticamente el retorno de los metadatos de la paginación junto con los elementos en esa página.

___

## Ejemplo de llamada al API

- **GET /list-dto?page=0&size=10** (esto devolverá la primera página con 10 productos)
- **GET /list-dto?page=1&size=5** (esto devolverá la segunda página con 5 productos)

En la respuesta JSON, además de los productos, obtendrás información de la paginación como el total de páginas, el total de elementos, etc...

Por ejemplo, ante esta petición **GET http://localhost:8080/productos/list-dto?page=0&size=3**, se obtiene el siguiente JSON de respuesta:

```
{
  "content": [
    {
      "nombre": "Producto 1 vaya",
      "sku": "SKU001",
      "precio": 1000
    },
    {
      "nombre": "Producto 2",
      "sku": "SKU002",
      "precio": 1500
    },
    {
      "nombre": "Producto 3",
      "sku": "SKU003",
      "precio": 2000
    }
  ],
  "pageable": {
    "pageNumber": 0,
    "pageSize": 3,
    "sort": {
      "empty": true,
      "sorted": false,
      "unsorted": true
    },
    "offset": 0,
    "paged": true,
    "unpaged": false
  },
  "last": false,
  "totalElements": 11,
  "totalPages": 4,
  "size": 3,
  "number": 0,
  "sort": {
    "empty": true,
    "sorted": false,
    "unsorted": true
  },
  "first": true,
  "numberOfElements": 3,
  "empty": false
}
```

## Ordenación de resultados

La ordenación de los resultados en Spring Data se puede hacer muy fácilmente utilizando el parámetro sort que Spring Data maneja automáticamente cuando usas un objeto Pageable.

### Ordenación Básica con el Parámetro sort:

El parámetro sort permite especificar el campo o los campos por los cuales quieres ordenar los resultados. 

Puedes añadirlo a tu URL de la siguiente forma:

- **?page=0&size=10&sort=nombre,asc** (para ordenar por el campo nombre en orden ascendente)
- **?page=0&size=10&sort=precio,desc** (para ordenar por el campo precio en orden descendente)

El parámetro sort permite ordenar por un único campo o varios campos. 

Si tienes más de un campo de ordenación, se pueden agregar más parámetros sort separados por comas, por ejemplo:

- **GET /list-dto?page=0&size=10&sort=nombre,asc,precio,desc**


## Validación de los parámetros page y size

### Validación Básica con Anotaciones:

Spring permite usar anotaciones de validación como @Min, @Max y @RequestParam para validar los parámetros.

```
@GetMapping("/list-dto")
public Page<ProductoDTO> findAllDTO(
    @RequestParam(defaultValue = "0") @Min(0) Integer page, 
    @RequestParam(defaultValue = "10") @Min(1) @Max(100) Integer size, 
    Pageable pageable) {

    // Si los parámetros son inválidos (por ejemplo, page < 0 o size < 1), 
    // Spring lanzará una excepción automáticamente (como ConstraintViolationException).
    
    // Recuperamos una página de productos con la paginación
    Page<Producto> productosPage = productoRepository.findAll(pageable);

    // Convertimos la página de productos a una página de DTOs
    Page<ProductoDTO> productosDTOPage = productosPage.map(p -> productoService.convert2ProductoDTO(p).get());

    return productosDTOPage;
}
```