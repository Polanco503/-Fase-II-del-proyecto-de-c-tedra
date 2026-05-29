package sv.edu.udb.restaurant.pedidos.service;

import java.math.BigDecimal;
import java.util.List;

import org.springframework.stereotype.Service;

import sv.edu.udb.restaurant.menu.model.Product;
import sv.edu.udb.restaurant.menu.repository.ProductRepository;
import sv.edu.udb.restaurant.pedidos.model.DetallePedido;
import sv.edu.udb.restaurant.pedidos.model.Order;
import sv.edu.udb.restaurant.pedidos.repository.DetallePedidoRepository;
import sv.edu.udb.restaurant.pedidos.repository.OrderRepository;

@Service
public class DetallePedidoService {

    private final DetallePedidoRepository detallePedidoRepository;
    private final OrderRepository orderRepository;
    private final ProductRepository productRepository;

    public DetallePedidoService(
            DetallePedidoRepository detallePedidoRepository,
            OrderRepository orderRepository,
            ProductRepository productRepository) {

        this.detallePedidoRepository = detallePedidoRepository;
        this.orderRepository = orderRepository;
        this.productRepository = productRepository;
    }

    public List<DetallePedido> findAll() {
        return detallePedidoRepository.findAll();
    }

    public List<DetallePedido> findByPedido(Long pedidoId) {
        return detallePedidoRepository.findByPedidoId(pedidoId);
    }

    public DetallePedido agregarProducto(
            Long pedidoId,
            Long productoId,
            Integer cantidad) {

        Order pedido = orderRepository.findById(pedidoId)
                .orElseThrow(() ->
                        new RuntimeException("Pedido no encontrado"));

        Product producto = productRepository.findById(productoId)
                .orElseThrow(() ->
                        new RuntimeException("Producto no encontrado"));

        if (producto.getStock() < cantidad) {
            throw new RuntimeException("Stock insuficiente");
        }

        producto.setStock(
                producto.getStock() - cantidad);

        productRepository.save(producto);

        BigDecimal subtotal = producto.getPrice()
                .multiply(BigDecimal.valueOf(cantidad));

        DetallePedido detalle = DetallePedido.builder()
                .pedido(pedido)
                .producto(producto)
                .cantidad(cantidad)
                .precioUnitario(producto.getPrice())
                .subtotal(subtotal)
                .build();

        return detallePedidoRepository.save(detalle);
    }

    public void delete(Long id) {
        detallePedidoRepository.deleteById(id);
    }
}