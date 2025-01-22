# Ejercicios de ampliación y refuerzo

- **Aplicación web:** Una solución completa con interfaz gráfica de usuario, diseñada para ser interactuada directamente por los usuarios a través de un navegador.

- **API REST:** Una interfaz de programación que permite a otras aplicaciones (o sistemas) interactuar con los datos y la lógica de vuelos, sin preocuparse de cómo se presenta esa información al usuario final.

## Ejercicio 1: gestión de vuelos

Crea una aplicación que ofrezca unos servicios web para la gestión de vuelos. 

La aplicación tendrá una base de datos de vuelos donde almacenará: origen, destino, precio, numero de escalas y compañia. 

Deberá ofrecer las siguientes operaciones:
- Búsqueda de vuelos, pudiendo filtrar por origen, destino y numero de escalas
- Registro de un nuevo vuelo
- Dar de baja un vuelo
- Dar de baja todos los vuelos a un destino determinado
- Modificar un vuelo

## Ejercicio 2: servicios web de búsqueda de hoteles

Crea una API que ofrezca servicios web de búsqueda de hoteles. 

Se mantendrá un base de datos de hoteles (nombre, descripción, categoría, ¿piscina?, localidad) y de las habitaciones de los mismos (tamaño, 1 ó 2 personas, precio/noche, ¿incluye desayuno?, ¿ocupada?). 

Deberá ofrecer, sobre esos datos, las siguientes operaciones:
- Búsqueda de hotel por localidad o categoría
- Búsqueda de habitaciones de un hotel por tamaño y precio (rango minimo→máximo). Solo mostrará aquellas habitaciones que estén marcadas como libres
- Registrar un nuevo hotel
- Registrar una nueva habitación a un hotel
- Eliminar una habitación determinada de un hotel
- Modificar una habitación para indicar que está ocupada