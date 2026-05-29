package sv.edu.udb.restaurant.pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record PublicOrderResponse(
        Long orderId,
        String orderNumber,
        String status,
        Long invoiceId,
        BigDecimal total,
        LocalDateTime createdAt,
        List<PublicOrderItemResponse> items
) {
}
