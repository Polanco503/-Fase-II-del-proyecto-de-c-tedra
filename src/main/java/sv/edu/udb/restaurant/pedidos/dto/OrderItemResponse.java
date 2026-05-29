package sv.edu.udb.restaurant.pedidos.dto;

import java.math.BigDecimal;

public record OrderItemResponse(
        String productName,
        Integer quantity,
        BigDecimal unitPrice,
        BigDecimal subtotal
) {
}
