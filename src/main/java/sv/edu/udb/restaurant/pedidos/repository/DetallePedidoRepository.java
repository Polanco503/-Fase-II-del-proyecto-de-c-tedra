package sv.edu.udb.restaurant.pedidos.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import sv.edu.udb.restaurant.pedidos.model.DetallePedido;

public interface DetallePedidoRepository extends JpaRepository<DetallePedido, Long> {

    List<DetallePedido> findByPedidoId(Long pedidoId);
}