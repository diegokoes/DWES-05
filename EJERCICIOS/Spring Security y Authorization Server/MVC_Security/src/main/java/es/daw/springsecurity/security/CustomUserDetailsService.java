package es.daw.springsecurity.security;

import es.daw.springsecurity.entity.User;
import es.daw.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import java.util.Collection;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CustomUserDetailsService implements UserDetailsService {

    private final UserRepository userRepository;

    /**
     * Se usará para autenticar usuarios en el login.
     * @param name
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    //public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
//        User user = userRepository.findByEmail(email)
//                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        User user = userRepository.findByUsername(name)
                .orElseThrow(() -> new UsernameNotFoundException("Usuario no encontrado"));

        System.out.println("*********************** USER DETAILS SERVICE **************");
        System.out.println(user);
        System.out.println("************************************************************");

        return new org.springframework.security.core.userdetails.User(
                user.getEmail(),
                user.getPassword(),
                user.getAuthorities()
        );
    }

    /**
     * Convierte los roles en GrantedAuthority para Spring Security.
     * @param user
     * @return
     */
    private Collection<? extends GrantedAuthority> getAuthorities(User user) {
        return user.getRoles().stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role.getName()))
                .collect(Collectors.toList());
    }
}
