package sv.edu.udb.restaurant.facturacion.dto;

import java.math.BigDecimal;
import java.time.LocalDateTime;

public record FacturaSummaryResponse(
        Long id,
        Long orderId,
        String orderNumber,
        LocalDateTime fechaFactura,
        BigDecimal subtotal,
        BigDecimal total,
        String estado
) {
}
