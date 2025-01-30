# Conceptos Claves de Seguridad en Aplicaciones Web

## Autenticación vs. Autorización

- **Autenticación:** Verificar la identidad de un usuario.
- **Autorización:** Determinar qué permisos tiene un usuario autenticado.

## JWT (JSON Web Token)

- Un formato de token basado en JSON usado para la autenticación y autorización sin estado.
- Compuesto por:
    - Header: Indica el algoritmo de firma (ej. HS256).
    - Payload: Contiene las reclamaciones (claims) como sub, roles, etc.
    - Firma: Generada con una clave secreta o certificado.

## Spring Security

- Framework de seguridad para aplicaciones Java con Spring.
- Permite implementar autenticación, autorización y protección contra ataques comunes.

# Ejemplo Práctico: Implementación de Spring Security con JWT

## Agregar dependencias

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
spring.jpa.hibernate.ddl-auto=update
```

- Con **create-drop** se crea todoas las tablas automáticamente y se eliminan cuando la aplicación se detiene.
- Con **update** se conservan las tablas entre ejecuciones.

## Cargar datos por defecto

Crear un archivo SQL en src/main/resources/import.sql 

Spring Boot ejecutará automáticamente los scripts SQL ubicados en src/main/resources cuando se arranque la aplicación. 

Pero, necesitas asegurarte de que los scripts de inicialización están habilitados y que la base de datos se cree correctamente.


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
-- Aquí estamos asignando roles a los usuarios mediante la tabla intermedia 'user_roles'
INSERT INTO user_roles (user_id, role_id) 
    SELECT u.id, r.id 
    FROM users u, roles r 
    WHERE u.username = 'admin' AND r.name = 'ROLE_ADMIN';

INSERT INTO user_roles (user_id, role_id) 
    SELECT u.id, r.id 
    FROM users u, roles r 
    WHERE u.username = 'user' AND r.name = 'ROLE_USER';

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

**@Builder** permite clear objetos con el patrón Builder:

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
```

## Implementar el Servicio de Usuarios

Ahora usaremos el repositorio para cargar usuarios desde la base de datos.

```
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
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado: " + username));
    }
}
```
UsernameNotFoundException es una excepción que forma parte de Spring Security

## Configurar Seguridad con Spring Security

## Crear un endpoint de registro y login

## Verificar que funciona


___
# Interceptores ?????