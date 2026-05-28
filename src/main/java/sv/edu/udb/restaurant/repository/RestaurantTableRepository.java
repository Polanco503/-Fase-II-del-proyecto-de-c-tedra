package sv.edu.udb.restaurant.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import sv.edu.udb.restaurant.model.RestaurantTable;

public interface RestaurantTableRepository extends JpaRepository<RestaurantTable, Long> {
}
