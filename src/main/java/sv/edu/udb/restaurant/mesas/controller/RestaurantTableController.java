package sv.edu.udb.restaurant.mesas.controller;

import jakarta.validation.Valid;
import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.restaurant.dto.MessageResponse;
import sv.edu.udb.restaurant.mesas.model.RestaurantTable;
import sv.edu.udb.restaurant.mesas.service.RestaurantTableService;

@RestController
@RequestMapping("/api/tables")
public class RestaurantTableController {

    private final RestaurantTableService restaurantTableService;

    public RestaurantTableController(
            RestaurantTableService restaurantTableService) {

        this.restaurantTableService = restaurantTableService;
    }

    @PostMapping
    public RestaurantTable create(
            @Valid @RequestBody RestaurantTable table) {

        table.setId(null);

        return restaurantTableService.save(table);
    }

    @GetMapping
    public List<RestaurantTable> findAll() {
        return restaurantTableService.findAll();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(@PathVariable Long id) {

        return restaurantTableService.findById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(new MessageResponse("Mesa no encontrada")));
    }

    @PutMapping("/{id}")
    public ResponseEntity<?> update(
            @PathVariable Long id,
            @Valid @RequestBody RestaurantTable table) {

        return restaurantTableService.findById(id)
                .<ResponseEntity<?>>map(existingTable -> {

                    existingTable.setTableNumber(
                            table.getTableNumber());

                    existingTable.setCapacity(
                            table.getCapacity());

                    return ResponseEntity.ok(
                            restaurantTableService.save(existingTable));
                })
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(new MessageResponse("Mesa no encontrada")));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(@PathVariable Long id) {

        if (!restaurantTableService.existsById(id)) {

            return ResponseEntity.status(404)
                    .body(new MessageResponse("Mesa no encontrada"));
        }

        restaurantTableService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}