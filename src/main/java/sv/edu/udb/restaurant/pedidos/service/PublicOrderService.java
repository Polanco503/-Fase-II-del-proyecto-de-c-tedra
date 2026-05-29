package sv.edu.udb.restaurant.pedidos.service;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import sv.edu.udb.restaurant.facturacion.model.EstadoFactura;
import sv.edu.udb.restaurant.facturacion.model.Factura;
import sv.edu.udb.restaurant.facturacion.repository.FacturaRepository;
import sv.edu.udb.restaurant.menu.model.Product;
import sv.edu.udb.restaurant.menu.repository.ProductRepository;
import sv.edu.udb.restaurant.pedidos.dto.PublicOrderItemRequest;
import sv.edu.udb.restaurant.pedidos.dto.PublicOrderItemResponse;
import sv.edu.udb.restaurant.pedidos.dto.PublicOrderRequest;
import sv.edu.udb.restaurant.pedidos.dto.PublicOrderResponse;
import sv.edu.udb.restaurant.pedidos.model.DetallePedido;
import sv.edu.udb.restaurant.pedidos.model.EstadoPedido;
import sv.edu.udb.restaurant.pedidos.model.Order;
import sv.edu.udb.restaurant.pedidos.repository.DetallePedidoRepository;
import sv.edu.udb.restaurant.pedidos.repository.OrderRepository;

@Service
public class PublicOrderService {

    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;
    private final DetallePedidoRepository detallePedidoRepository;
    private final FacturaRepository facturaRepository;

    public PublicOrderService(
            OrderRepository orderRepository,
            ProductRepository productRepository,
            DetallePedidoRepository detallePedidoRepository,
            FacturaRepository facturaRepository) {

        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
        this.detallePedidoRepository = detallePedidoRepository;
        this.facturaRepository = facturaRepository;
    }

    @Transactional
    public PublicOrderResponse createPublicOrder(
            PublicOrderRequest request) {

        Order order = Order.builder()
                .orderNumber(generateOrderNumber())
                .status(EstadoPedido.PENDIENTE)
                .createdAt(LocalDateTime.now())
                .build();

        Order savedOrder =
                orderRepository.save(order);

        List<DetallePedido> details =
                request.items()
                        .stream()
                        .map(item ->
                                createDetail(savedOrder, item))
                        .toList();

        BigDecimal total =
                details.stream()
                        .map(DetallePedido::getSubtotal)
                        .reduce(BigDecimal.ZERO, BigDecimal::add);

        Factura factura =
                Factura.builder()
                        .pedido(savedOrder)
                        .fechaFactura(LocalDateTime.now())
                        .subtotal(total)
                        .total(total)
                        .estado(EstadoFactura.PENDIENTE)
                        .build();

        Factura savedFactura =
                facturaRepository.save(factura);

        return new PublicOrderResponse(
                savedOrder.getId(),
                savedOrder.getOrderNumber(),
                savedOrder.getStatus().name(),
                savedFactura.getId(),
                savedFactura.getTotal(),
                savedOrder.getCreatedAt(),
                details.stream()
                        .map(detail ->
                                new PublicOrderItemResponse(
                                        detail.getProducto().getName(),
                                        detail.getCantidad(),
                                        detail.getPrecioUnitario(),
                                        detail.getSubtotal()))
                        .toList());
    }

    private DetallePedido createDetail(
            Order order,
            PublicOrderItemRequest item) {

        Product product =
                productRepository.findById(item.productId())
                        .orElseThrow(() ->
                                new IllegalArgumentException(
                                        "Producto no encontrado"));

        if (product.getStock() < item.quantity()) {

            throw new IllegalArgumentException(
                    "Stock insuficiente para "
                            + product.getName());
        }

        product.setStock(
                product.getStock() - item.quantity());

        productRepository.save(product);

        BigDecimal subtotal =
                product.getPrice()
                        .multiply(
                                BigDecimal.valueOf(
                                        item.quantity()));

        DetallePedido detail =
                DetallePedido.builder()
                        .pedido(order)
                        .producto(product)
                        .cantidad(item.quantity())
                        .precioUnitario(product.getPrice())
                        .subtotal(subtotal)
                        .build();

        return detallePedidoRepository.save(detail);
    }

    private String generateOrderNumber() {

        String prefix =
                LocalDate.now()
                        .format(
                                DateTimeFormatter.BASIC_ISO_DATE);

        long count =
                orderRepository.count() + 1;

        String orderNumber =
                prefix + "-" + String.format("%04d", count);

        while (orderRepository.existsByOrderNumber(orderNumber)) {

            count++;
            orderNumber =
                    prefix + "-" + String.format("%04d", count);
        }

        return orderNumber;
    }
}
