## COMPONENTES PRINCIPALES DE SPRING

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

## TIPOS DE CONTROLADORES

![image](https://github.com/user-attachments/assets/d7ac9301-1966-498f-b6f9-4afa004c31fe)


## ARCHIVOS DE CONFIGURACIÓN

**application.properties o application.yml:** Contienen la configuración de la aplicación, como:
- Configuración del servidor (server.port=8081)
- Detalles de la base de datos (spring.datasource.url=...)
- Configuraciones específicas de Spring Boot.

![image](https://github.com/user-attachments/assets/e9a970a5-119b-41ae-b98a-9a985b4eba44)

![image](https://github.com/user-attachments/assets/e1da308e-5a09-49ff-91de-3fe6c402c9d6)

## PRUEBAS (test/)

Incluyen pruebas unitarias (usualmente con JUnit y Mockito) y de integración.

![image](https://github.com/user-attachments/assets/52e9140c-1f43-48eb-b3b7-32b4ae6047e9)
