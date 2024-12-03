# Programación web con Spring Framework &amp; Spring Boot

<img src="https://github.com/user-attachments/assets/c781caf9-4052-4b11-b5bd-d8b34b53b106" height="200px"/>

## Primeros pasos

### Spring Boot ¿Qué es y cómo funciona?

https://www.arquitecturajava.com/spring-boot-que-es/

#### Estructura básica de un proyecto Spring

```
src/
 ├── main/
 │    ├── java/
 │    │    └── com.example.myapp/         // Paquete base de la aplicación
 │    │         ├── MyAppApplication.java // Clase principal con @SpringBootApplication
 │    │         ├── controller/           // Controladores REST
 │    │         ├── service/              // Lógica de negocio
 │    │         ├── repository/           // Acceso a la base de datos
 │    │         ├── model/                // Entidades (JPA, DTOs, etc.)
 │    │         └── config/               // Configuración personalizada
 │    └── resources/
 │         ├── application.properties     // Configuración principal de Spring Boot
 │         ├── application.yml            // (Alternativa) Configuración en formato YAML
 │         ├── static/                    // Archivos estáticos (HTML, CSS, JS)
 │         ├── templates/                 // Plantillas Thymeleaf o Freemarker
 │         └── db/                        // Scripts SQL (opcional)
 └── test/
      ├── java/                           // Pruebas unitarias y de integración

```

#### Componentes principales

**Clase principal:** es la clase que arranca la aplicación Spring Boot. Tiene la anotación @SpringBootApplication

![image](https://github.com/user-attachments/assets/8ee187f2-eb22-4bca-9491-2144356331d4)

**Controladores:** gestionan las solicitudes HTTP entrantes. Generalmente se anotan con @RestController o @Controller.

![image](https://github.com/user-attachments/assets/da6d1ec3-6e25-4642-a56e-014a43423d13)

**Servicios:** contienen la lógica de negocio. Se anotan con @Service.

![image](https://github.com/user-attachments/assets/15d61d3c-4c4f-42f4-96ee-c6f61fceea48)

**Repositorios:** interactúan con la base de datos. Usan Spring Data JPA u otros frameworks. Anotación típica: @Repository.

![image](https://github.com/user-attachments/assets/f12d44e6-c0b1-46d4-98e0-e935accacd9b)

**Entidades:** representan los objetos del domino y/o tablas de la base de datos. Se anotan con @Entity

![image](https://github.com/user-attachments/assets/fe61b599-0117-497e-a4bc-f5ff82f8f9ce)

**Configuración:** personalizan o extienden las configuraciones predeterminadas de Spring Boot. Usan anotaciones como @Configuration, @EnableWebSecurity, etc.

![image](https://github.com/user-attachments/assets/c022a1e1-6d0f-491c-9e6e-ba80d6f282ed)

#### Archivos de configuración 

**application.properties o application.yml:** Contienen la configuración de la aplicación, como:
- Configuración del servidor (server.port=8081)
- Detalles de la base de datos (spring.datasource.url=...)
- Configuraciones específicas de Spring Boot.

![image](https://github.com/user-attachments/assets/e9a970a5-119b-41ae-b98a-9a985b4eba44)

![image](https://github.com/user-attachments/assets/e1da308e-5a09-49ff-91de-3fe6c402c9d6)

#### Pruebas (test/)

Incluyen pruebas unitarias (usualmente con JUnit y Mockito) y de integración.

![image](https://github.com/user-attachments/assets/52e9140c-1f43-48eb-b3b7-32b4ae6047e9)



### Primera aplicación Spring "Hola Mundo"
https://www.jetbrains.com/help/idea/your-first-spring-application.html

### Segunda aplicación Spring

![image](https://github.com/user-attachments/assets/b5f4517e-07a3-48bc-9345-cf64c9dd93fa)

https://www.jetbrains.com/help/idea/spring-support-tutorial.html

## Webs de referencia

https://spring.io/

https://docs.spring.io/spring-framework/reference/web/webmvc.html

https://www.jetbrains.com/idea/spring/
___

## Página principal del curso
[VOLVER PÁGINA PRINCIPAL](https://github.com/profeMelola/DWES-00-2024-25)

## Licencia

<a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/"><img alt="Licencia de Creative Commons" style="border-width:0" src="https://i.creativecommons.org/l/by-nc-sa/4.0/88x31.png" /></a><br />Este obra está bajo una <a rel="license" href="http://creativecommons.org/licenses/by-nc-sa/4.0/">licencia de Creative Commons Reconocimiento-NoComercial-CompartirIgual 4.0 Internacional</a>.
