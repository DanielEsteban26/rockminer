package com.rockminer.service.impl;

import com.rockminer.dto.DetalleVentaDTO;
import com.rockminer.entity.DetalleVenta;
import com.rockminer.entity.Producto;
import com.rockminer.entity.Venta;
import com.rockminer.repository.DetalleVentaRepository;
import com.rockminer.repository.ProductoRepository;
import com.rockminer.repository.VentaRepository;
import com.rockminer.service.DetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleVentaServiceImpl implements DetalleVentaService {

    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;
    private final VentaRepository ventaRepository;

    @Override
    public List<DetalleVentaDTO> listarPorIdVenta(Long idVenta) {
        return detalleVentaRepository.findByVentaId(idVenta).stream()
                .map(detalle -> DetalleVentaDTO.builder()
                        .id(detalle.getId())
                        .cantidad(detalle.getCantidad())
                        .precioUnitario(detalle.getPrecioUnitario())
                        .subtotal(detalle.getCantidad() * detalle.getPrecioUnitario())
                        .idVenta(detalle.getVenta().getId())
                        .idProducto(detalle.getProducto().getId())
                        .nombreProducto(detalle.getProducto().getNombre())
                        .build())
                .collect(Collectors.toList());
    }

    @Override
    public DetalleVentaDTO registrar(DetalleVentaDTO dto) {
        Venta venta = ventaRepository.findById(dto.getIdVenta()).orElseThrow();
        Producto producto = productoRepository.findById(dto.getIdProducto()).orElseThrow();

        DetalleVenta detalle = DetalleVenta.builder()
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .venta(venta)
                .producto(producto)
                .build();

        detalleVentaRepository.save(detalle);

        return DetalleVentaDTO.builder()
                .id(detalle.getId())
                .cantidad(detalle.getCantidad())
                .precioUnitario(detalle.getPrecioUnitario())
                .subtotal(detalle.getCantidad() * detalle.getPrecioUnitario())
                .idVenta(venta.getId())
                .idProducto(producto.getId())
                .build();
    }
}
