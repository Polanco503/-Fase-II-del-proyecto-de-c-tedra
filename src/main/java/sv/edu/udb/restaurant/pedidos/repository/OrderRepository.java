package sv.edu.udb.restaurant.pedidos.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import sv.edu.udb.restaurant.pedidos.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {

    boolean existsByOrderNumber(String orderNumber);
}
