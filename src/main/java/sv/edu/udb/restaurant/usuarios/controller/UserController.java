package sv.edu.udb.restaurant.usuarios.controller;

import java.util.List;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import sv.edu.udb.restaurant.usuarios.dto.UserSummaryResponse;
import sv.edu.udb.restaurant.usuarios.service.UserService;

@RestController
@RequestMapping("/api/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserSummaryResponse> findAll() {
        return userService.findAll()
                .stream()
                .map(user ->
                        new UserSummaryResponse(
                                user.getEmail(),
                                user.getRole().name()))
                .toList();
    }
}
