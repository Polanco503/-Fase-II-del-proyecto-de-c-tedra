package sv.edu.udb.restaurant.controller;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sv.edu.udb.restaurant.dto.LoginRequest;
import sv.edu.udb.restaurant.dto.LoginResponse;
import sv.edu.udb.restaurant.dto.MessageResponse;
import sv.edu.udb.restaurant.security.JwtService;

import sv.edu.udb.restaurant.dto.RegisterRequest;
import sv.edu.udb.restaurant.model.enums.Role;

import sv.edu.udb.restaurant.usuarios.model.User;
import sv.edu.udb.restaurant.usuarios.repository.UserRepository;

@RestController
@RequestMapping("/api/auth")
public class AuthController {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtService jwtService;

    public AuthController(
            UserRepository userRepository,
            PasswordEncoder passwordEncoder,
            JwtService jwtService) {

        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtService = jwtService;
    }

    @PostMapping("/login")
    public ResponseEntity<?> login(
            @Valid @RequestBody LoginRequest request) {

        User user = userRepository.findByEmail(request.email())
                .orElse(null);

        if (user == null ||
                !passwordEncoder.matches(
                        request.password(),
                        user.getPassword())) {

            return ResponseEntity.status(HttpStatus.UNAUTHORIZED)
                    .body(new MessageResponse("Credenciales invalidas"));
        }

        String token = jwtService.generateToken(user);

        return ResponseEntity.ok(
                new LoginResponse(
                        token,
                        user.getEmail(),
                        user.getRole()));
    }

    @PostMapping("/register")
    public ResponseEntity<?> register(
            @Valid @RequestBody RegisterRequest request) {

        if (userRepository.findByEmail(request.email()).isPresent()) {

            return ResponseEntity.badRequest()
                    .body(new MessageResponse(
                            "El correo ya se encuentra registrado"));
        }

        Role role = request.role();

        if (role == null || role == Role.USUARIO) {

            return ResponseEntity.badRequest()
                    .body(new MessageResponse(
                            "Solo se pueden crear usuarios internos"));
        }

        User user = User.builder()
                .email(request.email())
                .password(
                        passwordEncoder.encode(
                                request.password()))
                .role(role)
                .build();

        userRepository.save(user);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(new MessageResponse(
                        "Usuario registrado correctamente"));
    }
}
