package sv.edu.udb.restaurant.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import sv.edu.udb.restaurant.model.enums.Role;

public record RegisterRequest(

        @Email
        @NotBlank
        String email,

        @NotBlank
        @Size(min = 6)
        String password,

        Role role

) {
}
