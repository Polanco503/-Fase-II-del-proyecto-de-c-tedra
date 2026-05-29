package sv.edu.udb.restaurant.dto;

import sv.edu.udb.restaurant.model.enums.Role;

public record LoginResponse(
        String token,
        String email,
        Role role
) {
}
