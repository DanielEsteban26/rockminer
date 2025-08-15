package com.rockminer.service.impl;

import com.lowagie.text.*;
import com.lowagie.text.pdf.*;
import com.rockminer.entity.DetalleVenta;
import com.rockminer.entity.Venta;
import com.rockminer.repository.DetalleVentaRepository;
import com.rockminer.repository.VentaRepository;
import com.rockminer.service.BoletaService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Service
public class BoletaServiceImpl implements BoletaService {

    @Autowired
    private VentaRepository ventaRepository;

    @Autowired
    private DetalleVentaRepository detalleVentaRepository;

    @Override
    public ByteArrayInputStream generarBoletaPDF(Long ventaId) {
        Venta venta = ventaRepository.findById(ventaId)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(ventaId);

        Document document = new Document(PageSize.A4);
        ByteArrayOutputStream out = new ByteArrayOutputStream();

        try {
            PdfWriter.getInstance(document, out);
            document.open();

            // Estilos
            Font titleFont = new Font(Font.HELVETICA, 20, Font.BOLD);
            Font subFont = new Font(Font.HELVETICA, 12, Font.BOLD);
            Font bodyFont = new Font(Font.HELVETICA, 10);

            // Encabezado
            Paragraph titulo = new Paragraph("BOLETA DE VENTA", titleFont);
            titulo.setAlignment(Element.ALIGN_CENTER);
            document.add(titulo);

            document.add(new Paragraph("Rockminer S.A.C.", subFont));
            document.add(new Paragraph("RUC: 12345678901", bodyFont));
            document.add(new Paragraph("Dirección: Lima, Perú", bodyFont));
            document.add(new Paragraph("Teléfono: (01) 123-4567", bodyFont));
            document.add(new Paragraph(" ")); // Espacio

            // Datos de la venta
            document.add(new Paragraph("Fecha: " + venta.getFecha().format(DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm")), bodyFont));
            document.add(new Paragraph("Vendido por: " + venta.getUsuario().getUsuario(), bodyFont));
            document.add(new Paragraph("Código de Venta: " + venta.getId(), bodyFont));
            document.add(new Paragraph(" "));

            // Tabla de productos
            PdfPTable tabla = new PdfPTable(4);
            tabla.setWidthPercentage(100);
            tabla.setWidths(new int[]{4, 1, 2, 2});

            tabla.addCell(getCell("Producto", subFont));
            tabla.addCell(getCell("Cant.", subFont));
            tabla.addCell(getCell("P. Unit.", subFont));
            tabla.addCell(getCell("Subtotal", subFont));

            double total = 0;

            for (DetalleVenta d : detalles) {
                tabla.addCell(getCell(d.getProducto().getNombre(), bodyFont));
                tabla.addCell(getCell(String.valueOf(d.getCantidad()), bodyFont));
                tabla.addCell(getCell(String.format("S/ %.2f", d.getPrecioUnitario()), bodyFont));
                double subtotal = d.getCantidad() * d.getPrecioUnitario();
                tabla.addCell(getCell(String.format("S/ %.2f", subtotal), bodyFont));
                total += subtotal;
            }

            document.add(tabla);

            // Cálculo del IGV
            double igv = total * 0.18;
            double subtotal = total - igv;

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Subtotal: S/ " + String.format("%.2f", subtotal), bodyFont));
            document.add(new Paragraph("IGV (18%): S/ " + String.format("%.2f", igv), bodyFont));
            document.add(new Paragraph("TOTAL: S/ " + String.format("%.2f", total), subFont));

            document.add(new Paragraph(" "));
            document.add(new Paragraph("Gracias por su compra.", bodyFont));

            document.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        return new ByteArrayInputStream(out.toByteArray());
    }

    private PdfPCell getCell(String text, Font font) {
        PdfPCell cell = new PdfPCell(new Phrase(text, font));
        cell.setHorizontalAlignment(Element.ALIGN_CENTER);
        cell.setPadding(5);
        return cell;
    }
}
