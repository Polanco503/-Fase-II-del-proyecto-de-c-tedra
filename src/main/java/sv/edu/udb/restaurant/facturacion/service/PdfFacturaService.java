package sv.edu.udb.restaurant.facturacion.service;

import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.springframework.stereotype.Service;

import com.lowagie.text.Document;
import com.lowagie.text.Font;
import com.lowagie.text.Paragraph;
import com.lowagie.text.pdf.PdfWriter;

import com.lowagie.text.pdf.PdfPTable;
import com.lowagie.text.pdf.PdfPCell;

import sv.edu.udb.restaurant.facturacion.model.Factura;
import sv.edu.udb.restaurant.pedidos.model.DetallePedido;
import sv.edu.udb.restaurant.pedidos.repository.DetallePedidoRepository;

@Service
public class PdfFacturaService {

    private final DetallePedidoRepository detallePedidoRepository;

    public PdfFacturaService(
            DetallePedidoRepository detallePedidoRepository) {

        this.detallePedidoRepository = detallePedidoRepository;
    }

    public byte[] generarFacturaPdf(Factura factura) {

        try {

            ByteArrayOutputStream outputStream =
                    new ByteArrayOutputStream();

            Document document = new Document();

            PdfWriter.getInstance(document, outputStream);

            document.open();

            Font titulo = new Font(
                    Font.HELVETICA,
                    18,
                    Font.BOLD);

            Font subtitulo = new Font(
                    Font.HELVETICA,
                    14,
                    Font.BOLD);

            document.add(
                    new Paragraph(
                            "===================================="));

            document.add(
                    new Paragraph(
                            "RESTAURANTE UDB",
                            titulo));

            document.add(
                    new Paragraph(
                            "===================================="));

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Factura #" + factura.getId()));

            document.add(
                    new Paragraph(
                            "Fecha: "
                                    + factura.getFechaFactura()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "dd/MM/yyyy"))));

            document.add(
                    new Paragraph(
                            "Hora: "
                                    + factura.getFechaFactura()
                                    .format(
                                            DateTimeFormatter.ofPattern(
                                                    "hh:mm a"))));

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Cliente: "
                                    + (factura.getPedido().getUser() == null
                                    ? "Cliente publico"
                                    : factura.getPedido()
                                            .getUser()
                                            .getEmail())));

            document.add(
                    new Paragraph(
                            "Orden: "
                                    + factura.getPedido()
                                    .getOrderNumber()));

            document.add(new Paragraph(" "));


            PdfPTable tabla = new PdfPTable(4);

            tabla.setWidthPercentage(100);

            tabla.addCell(new PdfPCell(new Paragraph("Producto")));
            tabla.addCell(new PdfPCell(new Paragraph("Cantidad")));
            tabla.addCell(new PdfPCell(new Paragraph("Precio")));
            tabla.addCell(new PdfPCell(new Paragraph("Subtotal")));

            List<DetallePedido> detalles =
                    detallePedidoRepository.findByPedidoId(
                            factura.getPedido().getId());

            for (DetallePedido detalle : detalles) {

                tabla.addCell(
                        detalle.getProducto().getName());

                tabla.addCell(
                        String.valueOf(
                                detalle.getCantidad()));

                tabla.addCell(
                        "$" + detalle.getPrecioUnitario());

                tabla.addCell(
                        "$" + detalle.getSubtotal());
            }

            document.add(tabla);






            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Subtotal: $"
                                    + factura.getSubtotal()));

            document.add(
                    new Paragraph(
                            "Total: $"
                                    + factura.getTotal(),
                            subtitulo));

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Estado: "
                                    + factura.getEstado(),
                            subtitulo));

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Documento generado automaticamente"));

            document.add(
                    new Paragraph(
                            "Restaurant UDB"));

            document.add(new Paragraph(" "));

            document.add(
                    new Paragraph(
                            "Gracias por su compra"));

            document.add(
                    new Paragraph(
                            "===================================="));

            document.close();

            return outputStream.toByteArray();

        } catch (Exception ex) {

            throw new RuntimeException(
                    "Error al generar PDF",
                    ex);
        }
    }
}
