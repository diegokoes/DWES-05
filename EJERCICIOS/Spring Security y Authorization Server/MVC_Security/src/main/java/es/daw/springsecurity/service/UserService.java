package es.daw.springsecurity.service;

import es.daw.springsecurity.entity.Role;
import es.daw.springsecurity.entity.User;
import es.daw.springsecurity.repository.RoleRepository;
import es.daw.springsecurity.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RoleRepository roleRepository;

    public void registerUser(User user) {

        user.setPassword(passwordEncoder.encode(user.getPassword()));

        System.out.println("************** REGISTER USER *********");
        System.out.println(user.getSelectedRole());
        System.out.println("********************************");

        Role role = roleRepository.findByName(user.getSelectedRole())
                .orElseThrow(() -> new RuntimeException("Role not found"));

        user.setRoles(Set.of(role));


        System.out.println("************** USUARIO A REGISTRAR ***********");
        System.out.println(user);
        System.out.println("*********************************************");

        userRepository.save(user);
    }

    public boolean loginUser(User user) {
        Optional<User> optUser = userRepository.findByUsername(user.getUsername());
        return optUser.isPresent();

        // PENDIENTE VALIDAR LA PWD... AQUÍ ENTRA SPRING SECURITY...
    }
}
