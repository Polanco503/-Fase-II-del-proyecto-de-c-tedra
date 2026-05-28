package sv.edu.udb.restaurant.config;

import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.password.PasswordEncoder;
import sv.edu.udb.restaurant.model.User;
import sv.edu.udb.restaurant.model.enums.Role;
import sv.edu.udb.restaurant.repository.UserRepository;

@Configuration
public class DataInitializer {

    @Bean
    public CommandLineRunner createInitialUsers(UserRepository userRepository, PasswordEncoder passwordEncoder) {
        return args -> {
            createUserIfMissing(userRepository, passwordEncoder, "admin@restaurant.com", "Admin123!", Role.ADMINISTRADOR);
            createUserIfMissing(userRepository, passwordEncoder, "mesero@restaurant.com", "Mesero123!", Role.MESERO);
            createUserIfMissing(userRepository, passwordEncoder, "usuario@restaurant.com", "Usuario123!", Role.USUARIO);
        };
    }

    private void createUserIfMissing(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            String email,
            String password,
            Role role
    ) {
        if (userRepository.findByEmail(email).isPresent()) {
            return;
        }

        User user = User.builder()
                .email(email)
                .password(passwordEncoder.encode(password))
                .role(role)
                .build();

        userRepository.save(user);
    }
}
