package sv.edu.udb.restaurant.facturacion.controller;

import java.util.List;

import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import sv.edu.udb.restaurant.dto.MessageResponse;
import sv.edu.udb.restaurant.facturacion.dto.FacturaSummaryResponse;
import sv.edu.udb.restaurant.facturacion.model.Factura;
import sv.edu.udb.restaurant.facturacion.service.FacturaService;
import sv.edu.udb.restaurant.facturacion.service.PdfFacturaService;

@RestController
@RequestMapping("/api/facturas")
public class FacturaController {

    private final FacturaService facturaService;
    private final PdfFacturaService pdfFacturaService;

    public FacturaController(
            FacturaService facturaService,
            PdfFacturaService pdfFacturaService) {

        this.facturaService = facturaService;
        this.pdfFacturaService = pdfFacturaService;
    }

    @GetMapping
    public List<FacturaSummaryResponse> findAll() {
        return facturaService.findAllSummaries();
    }

    @GetMapping("/{id}")
    public ResponseEntity<?> findById(
            @PathVariable Long id) {

        return facturaService.findSummaryById(id)
                .<ResponseEntity<?>>map(ResponseEntity::ok)
                .orElseGet(() ->
                        ResponseEntity.status(404)
                                .body(
                                        new MessageResponse(
                                                "Factura no encontrada")));
    }

    @PostMapping("/pedido/{pedidoId}")
    public Factura crearFactura(
            @PathVariable Long pedidoId) {

        return facturaService.crearFactura(
                pedidoId);
    }

    @PutMapping("/{id}/pagar")
    public Factura pagarFactura(
            @PathVariable Long id) {

        return facturaService.marcarComoPagada(
                id);
    }

    @GetMapping("/{id}/pdf")
    public ResponseEntity<byte[]> descargarPdf(
            @PathVariable Long id) {

        Factura factura =
                facturaService.findById(id)
                        .orElseThrow(() ->
                                new RuntimeException(
                                        "Factura no encontrada"));

        byte[] pdf =
                pdfFacturaService.generarFacturaPdf(
                        factura);

        return ResponseEntity.ok()
                .header(
                        HttpHeaders.CONTENT_DISPOSITION,
                        "attachment; filename=factura_"
                                + id
                                + ".pdf")
                .contentType(
                        MediaType.APPLICATION_PDF)
                .body(pdf);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<?> delete(
            @PathVariable Long id) {

        facturaService.deleteById(id);

        return ResponseEntity.noContent().build();
    }
}
