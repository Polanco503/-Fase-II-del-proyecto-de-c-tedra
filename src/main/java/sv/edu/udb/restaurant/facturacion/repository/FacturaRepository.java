package sv.edu.udb.restaurant.facturacion.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import sv.edu.udb.restaurant.facturacion.model.Factura;

public interface FacturaRepository extends JpaRepository<Factura, Long> {

    Optional<Factura> findByPedidoId(Long pedidoId);
}