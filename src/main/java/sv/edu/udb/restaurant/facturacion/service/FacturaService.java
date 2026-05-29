package sv.edu.udb.restaurant.facturacion.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.springframework.stereotype.Service;

import sv.edu.udb.restaurant.facturacion.dto.FacturaSummaryResponse;
import sv.edu.udb.restaurant.facturacion.model.EstadoFactura;
import sv.edu.udb.restaurant.facturacion.model.Factura;
import sv.edu.udb.restaurant.facturacion.repository.FacturaRepository;
import sv.edu.udb.restaurant.pedidos.model.DetallePedido;
import sv.edu.udb.restaurant.pedidos.model.Order;
import sv.edu.udb.restaurant.pedidos.repository.DetallePedidoRepository;
import sv.edu.udb.restaurant.pedidos.repository.OrderRepository;

@Service
public class FacturaService {

    private final FacturaRepository facturaRepository;
    private final OrderRepository orderRepository;
    private final DetallePedidoRepository detallePedidoRepository;

    public FacturaService(
            FacturaRepository facturaRepository,
            OrderRepository orderRepository,
            DetallePedidoRepository detallePedidoRepository) {

        this.facturaRepository = facturaRepository;
        this.orderRepository = orderRepository;
        this.detallePedidoRepository = detallePedidoRepository;
    }

    public List<Factura> findAll() {
        return facturaRepository.findAll();
    }

    public List<FacturaSummaryResponse> findAllSummaries() {

        return facturaRepository.findAll()
                .stream()
                .map(this::toSummary)
                .toList();
    }

    public Optional<Factura> findById(Long id) {
        return facturaRepository.findById(id);
    }

    public Optional<FacturaSummaryResponse> findSummaryById(Long id) {

        return facturaRepository.findById(id)
                .map(this::toSummary);
    }

    public Factura crearFactura(Long pedidoId) {

        if (facturaRepository.findByPedidoId(pedidoId).isPresent()) {

            throw new RuntimeException(
                    "Ya existe una factura para este pedido");
        }

        Order pedido = orderRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Pedido no encontrado"));

        List<DetallePedido> detalles =
                detallePedidoRepository.findByPedidoId(
                        pedidoId);

        if (detalles.isEmpty()) {

            throw new RuntimeException(
                    "El pedido no tiene productos");
        }

        BigDecimal total = detalles.stream()
                .map(DetallePedido::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        Factura factura = Factura.builder()
                .pedido(pedido)
                .fechaFactura(LocalDateTime.now())
                .subtotal(total)
                .total(total)
                .estado(EstadoFactura.PENDIENTE)
                .build();

        return facturaRepository.save(factura);
    }

    public Factura marcarComoPagada(Long id) {

        Factura factura = facturaRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException(
                                "Factura no encontrada"));

        factura.setEstado(
                EstadoFactura.PAGADA);

        return facturaRepository.save(
                factura);
    }

    public void deleteById(Long id) {
        facturaRepository.deleteById(id);
    }

    private FacturaSummaryResponse toSummary(Factura factura) {

        Order order =
                factura.getPedido();

        return new FacturaSummaryResponse(
                factura.getId(),
                order.getId(),
                order.getOrderNumber(),
                factura.getFechaFactura(),
                factura.getSubtotal(),
                factura.getTotal(),
                factura.getEstado().name());
    }
}
