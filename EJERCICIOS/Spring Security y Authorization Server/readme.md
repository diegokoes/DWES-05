# Conceptos Claves de Seguridad en Aplicaciones Web

## Autenticación vs. Autorización

![alt text](image-1.png)


## JWT (JSON Web Token)

![alt text](image-2.png)

![alt text](image-4.png)

![alt text](image-7.png)

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

## Nuevo paquete security

HASTA AQUÍ!!!!! 

![alt text](image-5.png)
_______

- SecurityConfig.
- PasswordEnconderConfig


_______

https://www.unlogged.io/post/integrating-jwt-with-spring-security-6-in-spring-boot-3

### Implementación de JwtUtil

Esta clase genera y valida los tokens JWT.

Es necesario añadir manualmente la dependencia a **Java JWT**.

```
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-api</artifactId>
            <version>0.12.3</version>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-impl</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>
        <dependency>
            <groupId>io.jsonwebtoken</groupId>
            <artifactId>jjwt-jackson</artifactId>
            <version>0.12.3</version>
            <scope>runtime</scope>
        </dependency>


```


Esta es la clase de utilidades:

```
import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.function.Function;

import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.Claims;

@Component
public class JwtUtil {
    private static final String SECRET_KEY = "tu_clave_secreta_super_segura_de_32_bytes_minimo";

    private Key getSigningKey() {
        byte[] keyBytes = Decoders.BASE64.decode(SECRET_KEY);
        return Keys.hmacShaKeyFor(keyBytes);
    }

    public String generateToken(String username) {
        return createToken(new HashMap<>(), username);
    }

    private String createToken(Map<String, Object> claims, String subject) {
        return Jwts.builder()
                .setClaims(claims)
                .setSubject(subject)
                .setIssuedAt(new Date(System.currentTimeMillis()))
                .setExpiration(new Date(System.currentTimeMillis() + 1000 * 60 * 60)) // 1 hora
                .signWith(getSigningKey(), SignatureAlgorithm.HS256)
                .compact();
    }

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, Claims::getExpiration);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }

    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    public boolean isTokenValid(String token, UserDetails userDetails) {
        final String username = extractUsername(token);
        return (username.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }
}

```

### Implementación de JwtAuthenticationFilter

Este filtro se ejecuta en cada petición para verificar el token JWT:

```
@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtUtil jwtUtil;
    private final UserDetailsService userDetailsService;

    public JwtAuthenticationFilter(JwtUtil jwtUtil, UserDetailsService userDetailsService) {
        this.jwtUtil = jwtUtil;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain chain)
            throws ServletException, IOException {
        String authorizationHeader = request.getHeader("Authorization");

        if (authorizationHeader == null || !authorizationHeader.startsWith("Bearer ")) {
            chain.doFilter(request, response);
            return;
        }

        String token = authorizationHeader.substring(7);
        String username = jwtUtil.extractUsername(token);

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);

            if (jwtUtil.isTokenValid(token, userDetails)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }

        chain.doFilter(request, response);
    }
}
```

### Configurar Seguridad con Spring Security

```
@Configuration
public class SecurityConfig {
    private final UserService userService;
    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(UserService userService, JwtAuthenticationFilter jwtAuthenticationFilter) {

        this.userService = userService;
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(auth -> auth
                        .requestMatchers("/auth/login").permitAll()
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }

    @Bean
    public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
        return authenticationConfiguration.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
```
 **SecurityFilterChain:** Configura las reglas de seguridad de la aplicación.

## Crear un endpoint de registro y login

Ahora permitimos a los usuarios registrarse y autenticarse.

```
@RestController
@RequestMapping("/auth")
public class AuthController {
    private final AuthenticationManager authenticationManager;
    private final UserRepository userRepository;
    private final JwtUtil jwtUtil;
    private final PasswordEncoder passwordEncoder;

    public AuthController(AuthenticationManager authenticationManager, UserRepository userRepository, JwtUtil jwtUtil, PasswordEncoder passwordEncoder) {
        this.authenticationManager = authenticationManager;
        this.userRepository = userRepository;
        this.jwtUtil = jwtUtil;
        this.passwordEncoder = passwordEncoder;
    }

    @PostMapping("/register")
    public String register(@RequestBody Map<String, String> request) {
        User user = new User();
        user.setUsername(request.get("username"));
        user.setPassword(passwordEncoder.encode(request.get("password")));
        user.setRoles(Collections.singleton(new Role(1L, "ROLE_USER")));

        userRepository.save(user);
        return "Usuario registrado con éxito!";
    }

    @PostMapping("/login")
    public Map<String, String> login(@RequestBody Map<String, String> request) {
        authenticationManager.authenticate(new UsernamePasswordAuthenticationToken(
                request.get("username"), request.get("password")));

        String token = jwtUtil.generateToken(request.get("username"));
        return Map.of("token", token);
    }
}
```
## Verificar que funciona

- Inicia la aplicación y accede a http://localhost:8080/h2-console para ver la base de datos.
- Registra un usuario con POST /auth/register:

```
{
    "username": "admin",
    "password": "password"
}

```
- Inicia sesión con POST /auth/login, recibirás un JWT en la respuesta.
- Usa el JWT para acceder a otros endpoints protegidos.

___
# Interceptores ?????