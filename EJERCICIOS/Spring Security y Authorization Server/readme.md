# Conceptos Claves de Seguridad en Aplicaciones Web

## Autenticación vs. Autorización

![alt text](image-1.png)


## JWT (JSON Web Token)

![alt text](image-2.png)

![alt text](image-4.png)

___

![alt text](image-7.png)

![alt text](image-8.png)

*Fuente: Udemy - Construye aplicaciones web Spring Framework 6 y Spring Boot 3*

___

https://jwt.io/

![alt text](image-6.png)



Cuando un usuario haga login (inicie sesión), en vez de trabajar con sesiones, como se hace tradicionalmente, que consume recursos de backend (se genera una cookie, se consume memoria porque ocupa recursos), se genera un token con cierta información (payload).

El token tendrá información no sensible:

- Fecha de generación.
- Fecha de expedición.
- Nombre.
- Email.
- No tendrá la password...

Esta información se firma con un algoritmo, con una clave privada que solo conoce la apliación de Spring Boot.

Cuando a la aplicación le llega el token, la aplicación intenta decodificarlo con esa clave:

- Si se puede decodificar, el token lo ha creado la apliación y se da acceso.
- Si no se puede decodificar, no confiamos en él y no se da acceso.

## Spring Security

![alt text](image-3.png)

# Ejemplo Práctico: Implementación de Spring Security con JWT

## Crea el proyecto y agregar dependencias

El proyecto Spring Boot debe llamarse **SpringSecurity**.

El "Group" y "Package Name" serán **es.daw.springsecurity**

Buscamos *Security* en Starters del pom.xml:

![alt text](image.png)

## Configurar H2 en memoria

```
# Configuración de H2 en memoria
spring.datasource.url=jdbc:h2:mem:testdb
spring.datasource.driverClassName=org.h2.Driver
spring.datasource.username=sa
spring.datasource.password=
spring.jpa.database-platform=org.hibernate.dialect.H2Dialect

# Habilitar consola H2 (acceder en: http://localhost:8080/h2-console)
spring.h2.console.enabled=true
spring.h2.console.path=/h2-console

# Hacer que Hibernate recree la base de datos en cada reinicio
spring.jpa.hibernate.ddl-auto=create-drop
```

- Con **create-drop** se crea todas las tablas automáticamente y se eliminan cuando la aplicación se detiene.
- Con **update** se conservan las tablas entre ejecuciones. Si en el futuro quieres que los datos persistan entre reinicios, lo adecuado es cambiar la base de datos a file, no mem y configurar update.

## Cargar datos por defecto

Crear un archivo SQL en **src/main/resources/import.sql**

Spring Boot ejecutará automáticamente los scripts SQL ubicados en src/main/resources cuando se arranque la aplicación. 


**Contenido de import.sql:**

```
-- Crear roles por defecto
INSERT INTO roles (name) VALUES ('ROLE_USER');
INSERT INTO roles (name) VALUES ('ROLE_ADMIN');

-- Crear usuarios por defecto con sus roles
-- Asegúrate de que la tabla 'users' y 'user_roles' existen
INSERT INTO users (username, password) VALUES ('admin', '{bcrypt}password');
INSERT INTO users (username, password) VALUES ('user', '{bcrypt}password');

-- Asignar roles a usuarios
INSERT INTO user_roles (user_id, role_id) VALUES (1,2);
INSERT INTO user_roles (user_id, role_id) VALUES (2,1);
```

Pendiente generar contraseñas encriptadas con BCryptPasswordEncoder ....

## Crear Entidades de Usuario y Rol

Entidad Role:

```
@Entity
@Table(name = "roles")
@Data
@NoArgsConstructor
@AllArgsConstructor
public class Role {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String name;

    // Relación inversa con 'User'
    @ManyToMany(mappedBy = "roles")  // 'roles' es el nombre del atributo en la entidad User
    private Set<User> users;    
}

```

Entidad User:

```
@Entity
@Table(name = "users")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class User implements UserDetails {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(unique = true, nullable = false)
    private String username;

    @Column(nullable = false)
    private String password;

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
        name = "user_roles",
        joinColumns = @JoinColumn(name = "user_id"),
        inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toSet());
    }

    @Override
    public boolean isAccountNonExpired() { return true; }

    @Override
    public boolean isAccountNonLocked() { return true; }

    @Override
    public boolean isCredentialsNonExpired() { return true; }

    @Override
    public boolean isEnabled() { return true; }
}

```

Spring Security trabaja con un sistema de autenticación basado en **UserDetailsService**, que carga los usuarios desde la base de datos. Al **implementar UserDetails**, tu entidad User es compatible con Spring Security y puedes personalizar la lógica de autenticación y autorización.

Por otro lado, la anotación **@Builder** permite crear objetos con el patrón Builder:

```
User user = User.builder()
    .username("admin")
    .password("password")
    .roles(Set.of(new Role(1L, "ROLE_USER")))
    .build();

```

Sin usar @Builder:

```
User user = new User();
user.setUsername("admin");
user.setPassword("password");
user.setRoles(Set.of(new Role(1L, "ROLE_USER")));

```

En nuestro caso, Lombok genere automáticamente:

```
public static UserBuilder builder() {
    return new UserBuilder();
}

```

**Cuidado!!!** No es recomendable declarar User como un @Bean en el contexto de Spring. Las entidades (@Entity) deben ser gestionadas por JPA y no por el contenedor de Spring.

## Crear Repositorios

Repositorio de usuarios:

```
public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByUsername(String username);
}

public interface RoleRepository extends JpaRepository<Role, Long> {
    Optional<Role> findByName(String name);
}

```

## Implementar el Servicio de Usuarios

Ahora usaremos el repositorio para cargar usuarios desde la base de datos.

```
@Slf4j
@Service
public class UserService implements UserDetailsService {
    private final UserRepository userRepository;

    @Autowired
    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        return userRepository.findByUsername(username)
                .orElseThrow(() -> {
                    log.warn("Intento de login con usuario no encontrado: {}", username);
                    return new UsernameNotFoundException("Usuario no encontrado: " + username);
                });
    }
}
```
UsernameNotFoundException es una excepción que forma parte de Spring Security

## Nuevo paquete security

HASTA AQUÍ!!!!! 

![alt text](image-5.png)

### Dependencia Java JWT


Es necesario añadir manualmente la dependencia a **Java JWT**.

```
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.6</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.6</version>
            <scope>runtime</scope>
        </dependency>
```

### Sigue las instrucciones del profesor para montar todas las clases necesarias en el paquete Security

![alt text](image-9.png)


## AuthController

**POST /auth/login**

    - Recibe un usuario y contraseña.
    - Autentica al usuario.
    - Genera un JWT y lo devuelve en la respuesta.

**POST /auth/register**

    - Recibe un usuario y contraseña.
    - Registra un nuevo usuario en la base de datos.
    - Devuelve un mensaje de éxito.

```
POST http://localhost:8080/auth/register
Content-Type: application/json

{
    "username": "usuario123",
    "password": "password123"
}
```