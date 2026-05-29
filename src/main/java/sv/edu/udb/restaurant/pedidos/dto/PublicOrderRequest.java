package sv.edu.udb.restaurant.pedidos.dto;

import java.util.List;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;

public record PublicOrderRequest(

        @Valid
        @NotEmpty(message = "El pedido debe incluir al menos un producto")
        List<PublicOrderItemRequest> items
) {
}
