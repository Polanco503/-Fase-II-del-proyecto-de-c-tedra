package sv.edu.udb.restaurant.pedidos.service;

import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sv.edu.udb.restaurant.facturacion.repository.FacturaRepository;
import sv.edu.udb.restaurant.pedidos.dto.OrderItemResponse;
import sv.edu.udb.restaurant.pedidos.dto.OrderSummaryResponse;
import sv.edu.udb.restaurant.pedidos.model.DetallePedido;
import sv.edu.udb.restaurant.pedidos.model.EstadoPedido;
import sv.edu.udb.restaurant.pedidos.model.Order;
import sv.edu.udb.restaurant.pedidos.repository.DetallePedidoRepository;
import sv.edu.udb.restaurant.pedidos.repository.OrderRepository;

@Service
public class OrderService {

    private final OrderRepository orderRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final FacturaRepository facturaRepository;

    public OrderService(
            OrderRepository orderRepository,
            DetallePedidoRepository detallePedidoRepository,
            FacturaRepository facturaRepository) {

        this.orderRepository = orderRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.facturaRepository = facturaRepository;
    }

    public List<Order> findAll() {
        return orderRepository.findAll();
    }

    public Optional<Order> findById(Long id) {
        return orderRepository.findById(id);
    }

    public List<OrderSummaryResponse> findAllSummaries() {

        return orderRepository.findAll()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public Optional<OrderSummaryResponse> findSummaryById(Long id) {

        return orderRepository.findById(id)
                .map(this::toSummary);
    }

    public Order save(Order order) {
        return orderRepository.save(order);
    }

    public OrderSummaryResponse updateStatus(
            Long id,
            EstadoPedido status) {

        Order order =
                orderRepository.findById(id)
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Pedido no encontrado"));

        order.setStatus(status);

        return toSummary(
                orderRepository.save(order));
    }

    public boolean existsById(Long id) {
        return orderRepository.existsById(id);
    }

    public void deleteById(Long id) {
        orderRepository.deleteById(id);
    }

    private OrderSummaryResponse toSummary(Order order) {

        List<DetallePedido> details =
                detallePedidoRepository.findByPedidoId(
                        order.getId());

        return new OrderSummaryResponse(
                order.getId(),
                order.getOrderNumber(),
                order.getStatus() == null
                        ? "PENDIENTE"
                        : order.getStatus().name(),
                facturaRepository.findByPedidoId(order.getId())
                        .map(factura -> factura.getTotal())
                        .orElseGet(() ->
                                details.stream()
                                        .map(DetallePedido::getSubtotal)
                                        .reduce(
                                                java.math.BigDecimal.ZERO,
                                                java.math.BigDecimal::add)),
                order.getCreatedAt(),
                details.stream()
                        .map(detail ->
                                new OrderItemResponse(
                                        detail.getProducto().getName(),
                                        detail.getCantidad(),
                                        detail.getPrecioUnitario(),
                                        detail.getSubtotal()))
                        .toList());
    }
}
