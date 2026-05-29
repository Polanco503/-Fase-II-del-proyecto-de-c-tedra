package sv.edu.udb.restaurant.pedidos.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

public record OrderSummaryResponse(
        Long id,
        String orderNumber,
        String status,
        BigDecimal total,
        LocalDateTime createdAt,
        List<OrderItemResponse> items
) {
}
