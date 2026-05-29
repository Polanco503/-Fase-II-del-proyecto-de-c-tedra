package sv.edu.udb.restaurant.pedidos.dto;

import jakarta.validation.constraints.NotNull;
import sv.edu.udb.restaurant.pedidos.model.EstadoPedido;

public record UpdateOrderStatusRequest(

        @NotNull(message = "El estado es obligatorio")
        EstadoPedido status
) {
}
