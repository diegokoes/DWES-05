package es.daw.springsecurity.entity;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import java.util.*;
import java.util.stream.Collectors;

@Entity
@Table(name = "users")
@Data
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

    // ------------------ OWM --------------------------
    //@Column(nullable = false, unique = true)
    //private String email;

    // Permisos que tiene un usuario
    // Se utiliza para mapear colecciones de elementos embebidos que no representan una entidad por sí misma,
    // sino que son parte de la entidad principal.
    // JPA creará automáticamente una tabla de colección para guardar esas enumeraciones.
    //@ElementCollection(fetch = FetchType.EAGER) // un usuario no tiene mucho authorities... mejor precargado
    //@Enumerated(EnumType.STRING) // Cada valor de la enumeración sea almacenado como una cadena
    //private List<UserAuthority> authorities = new ArrayList<>();

    /*
        Un rol puede ser "ADMIN", que implica un conjunto de permisos.
        Una autoridad puede ser un permiso específico como "READ_PRIVILEGES" o "WRITE_PRIVILEGES".
     */
    // -------------------------------------------------

    @ManyToMany(fetch = FetchType.EAGER)
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "role_id")
    )
    private Set<Role> roles;

    public User(){
        roles = new HashSet<>();
    }

    public void addRole(Role role) {
        roles.add(role);
        //role.getUsers().add(this);
    }

    public void removeRole(Role role) {
        roles.remove(role);
        //role.getUsers().remove(this);
    }

    // ---------------------------------------------------------------------
    /*
    Este método convierte la lista de roles del usuario (roles) en una colección de autoridades (GrantedAuthority).
    En Spring Security, una autoridad representa un permiso o un rol que se asigna a un usuario.
    Spring Security usa GrantedAuthority para determinar los permisos de un usuario en el sistema.
    El método getAuthorities() es utilizado por Spring Security para autenticar y autorizar al usuario.
    De: roles = [ new Role("ADMIN"), new Role("USER") ]
    A: Set<GrantedAuthority> authorities = ["ADMIN", "USER"]
     */
    @Override
    public Collection<? extends GrantedAuthority> getAuthorities() {
        return roles.stream()
                .map(role -> (GrantedAuthority) role::getName)
                .collect(Collectors.toSet());
//        return this.authorities.stream()
//                .map(authority -> new SimpleGrantedAuthority(authority.toString()))
//                .toList();
    }

    /**
     * Indica si la cuenta del usuario ha expirado.
     * A true: la cuenta nunca expira
     * @return
     */
    @Override
    public boolean isAccountNonExpired() { return true; }

    /**
     * Indica si la cuenta está bloqueada.
     * A true: la cuenta no está bloqueada.
     * @return
     */
    @Override
    public boolean isAccountNonLocked() { return true; }

    /**
     * Indica si las credenciales (contraseña) han expirado.
     * A true: la contraseña nunca expira
     * @return
     */
    @Override
    public boolean isCredentialsNonExpired() { return true; }

    /**
     * Indica si la cuenta está habilitada.
     * A true: la cuenta está activa
     * @return
     */
    @Override
    public boolean isEnabled() { return true; }


    // ------------ SI NO DA PROBLEMAS HIBERNATE ------------------------
    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        User user = (User) o;
        return Objects.equals(id, user.id);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(id);
    }

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                '}';
    }
}

