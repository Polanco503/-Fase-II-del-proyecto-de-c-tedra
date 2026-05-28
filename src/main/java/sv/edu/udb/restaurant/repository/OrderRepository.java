package sv.edu.udb.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.restaurant.model.Order;

public interface OrderRepository extends JpaRepository<Order, Long> {
}
