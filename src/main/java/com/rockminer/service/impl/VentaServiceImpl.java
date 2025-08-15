package com.rockminer.service.impl;

import com.rockminer.dto.DetalleVentaDTO;
import com.rockminer.dto.VentaDTO;
import com.rockminer.entity.DetalleVenta;
import com.rockminer.entity.Producto;
import com.rockminer.entity.Usuario;
import com.rockminer.entity.Venta;
import com.rockminer.exception.ResourceNotFoundException;
import com.rockminer.repository.DetalleVentaRepository;
import com.rockminer.repository.ProductoRepository;
import com.rockminer.repository.UsuarioRepository;
import com.rockminer.repository.VentaRepository;
import com.rockminer.service.VentaService;
import jakarta.persistence.EntityNotFoundException;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;


import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class VentaServiceImpl implements VentaService {

    private final VentaRepository ventaRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final DetalleVentaRepository detalleVentaRepository;
    private final ProductoRepository productoRepository;

    @Override
    public List<VentaDTO> listar() {
        return ventaRepository.findAll().stream()
                .filter(venta -> !venta.isAnulado()) // Solo ventas no anuladas
                .map(venta -> {
                    VentaDTO dto = modelMapper.map(venta, VentaDTO.class);
                    if (venta.getUsuario() != null) {
                        dto.setIdUsuario(venta.getUsuario().getId());
                    }

                    List<DetalleVentaDTO> detalles = detalleVentaRepository.findByVentaId(venta.getId())
                            .stream()
                            .map(detalle -> {
                                Producto producto = detalle.getProducto();
                                return DetalleVentaDTO.builder()
                                        .id(detalle.getId())
                                        .cantidad(detalle.getCantidad())
                                        .precioUnitario(detalle.getPrecioUnitario())
                                        .subtotal(detalle.getPrecioUnitario() * detalle.getCantidad())
                                        .idVenta(venta.getId())
                                        .idProducto(producto.getId())
                                        .nombreProducto(producto.getNombre())
                                        .build();
                            })
                            .collect(Collectors.toList());

                    dto.setDetalles(detalles);

                    return dto;
                })
                .collect(Collectors.toList());
    }



    @Override
    public VentaDTO obtenerPorId(Long id) {
        Venta venta = ventaRepository.findById(id)
                .filter(v -> !v.isAnulado()) // Solo si no está anulada
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada o anulada"));
        VentaDTO dto = modelMapper.map(venta, VentaDTO.class);
        if (venta.getUsuario() != null) {
            dto.setIdUsuario(venta.getUsuario().getId());
        }

        List<DetalleVentaDTO> detalles = detalleVentaRepository.findByVentaId(venta.getId())
                .stream()
                .map(detalle -> {
                    Producto producto = detalle.getProducto();
                    return DetalleVentaDTO.builder()
                            .id(detalle.getId())
                            .cantidad(detalle.getCantidad())
                            .precioUnitario(detalle.getPrecioUnitario())
                            .subtotal(detalle.getPrecioUnitario() * detalle.getCantidad())
                            .idVenta(venta.getId())
                            .idProducto(producto.getId())
                            .nombreProducto(producto.getNombre())
                            .build();
                })
                .collect(Collectors.toList());

        dto.setDetalles(detalles);

        return dto;
    }


    @Override
    public VentaDTO registrar(VentaDTO dto) {
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        Venta venta = new Venta();
        venta.setFecha(dto.getFecha());
        venta.setUsuario(usuario);
        venta.setAnulado(false);

        venta = ventaRepository.save(venta); // guardar primero para asignar ID

        double totalCalculado = 0.0;
        List<DetalleVentaDTO> detallesRegistrados = new ArrayList<>();

        for (DetalleVentaDTO detalleDTO : dto.getDetalles()) {
            Producto producto = productoRepository.findById(detalleDTO.getIdProducto())
                    .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

            double precioUnitario = producto.getPrecioUnitario(); // aquí lo traes del producto
            int cantidad = detalleDTO.getCantidad();
            double subtotal = precioUnitario * cantidad;
            totalCalculado += subtotal;

            DetalleVenta detalle = DetalleVenta.builder()
                    .venta(venta)
                    .producto(producto)
                    .cantidad(cantidad)
                    .precioUnitario(precioUnitario)
                    .build();

            detalle = detalleVentaRepository.save(detalle);

            // actualizar stock
            producto.setStock(producto.getStock() - cantidad);
            productoRepository.save(producto);

            detallesRegistrados.add(DetalleVentaDTO.builder()
                    .id(detalle.getId())
                    .cantidad(cantidad)
                    .precioUnitario(precioUnitario)
                    .subtotal(subtotal)
                    .idVenta(venta.getId())
                    .idProducto(producto.getId())
                    .nombreProducto(producto.getNombre()) // ← aquí se agrega el nombre del producto
                    .build());
        }

        venta.setTotal(totalCalculado);
        ventaRepository.save(venta);

        return VentaDTO.builder()
                .id(venta.getId())
                .fecha(venta.getFecha())
                .total(venta.getTotal())
                .idUsuario(usuario.getId())
                .detalles(detallesRegistrados)
                .build();
    }


    //nuna se usaria actualizar porque si se falla en una venta solo se elimina y se crea
    //nuevamente otra venta.
    @Override
    public VentaDTO actualizar(Long id, VentaDTO dto) {
        Venta venta = ventaRepository.findById(id).filter(v -> !v.isAnulado())
                .orElseThrow(() -> new EntityNotFoundException("Venta no encontrada o está anulada"));
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow();

        venta.setFecha(dto.getFecha());
        venta.setTotal(dto.getTotal());
        venta.setUsuario(usuario);

        ventaRepository.save(venta);
        dto.setId(id);
        return dto;
    }

    @Override
    @Transactional
    public void anularVenta(Long idVenta) {
        Venta venta = ventaRepository.findById(idVenta)
                .orElseThrow(() -> new ResourceNotFoundException("Venta no encontrada con ID: " + idVenta));

        if (venta.isAnulado()) {
            throw new IllegalStateException("La venta ya está anulada");
        }

        // 1. Primero se marca la venta como anulada
        venta.setAnulado(true);
        ventaRepository.save(venta);

        // 2. Luego se revierte el stock
        List<DetalleVenta> detalles = detalleVentaRepository.findByVentaId(idVenta);
        for (DetalleVenta detalle : detalles) {
            Producto producto = productoRepository.findById(detalle.getProducto().getId())
                    .orElseThrow(() -> new ResourceNotFoundException("Producto no encontrado con ID: " + detalle.getProducto().getId()));
            producto.setStock(producto.getStock() + detalle.getCantidad());
            productoRepository.save(producto);
        }
    }


}
