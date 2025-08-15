package com.rockminer.service.impl;

import com.rockminer.dto.CompraDTO;
import com.rockminer.dto.DetalleCompraDTO;
import com.rockminer.dto.DetalleVentaDTO;
import com.rockminer.entity.*;
import com.rockminer.exception.PrecioDiferenteException;
import com.rockminer.repository.*;
import com.rockminer.service.CompraService;
import jakarta.persistence.EntityNotFoundException;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompraServiceImpl implements CompraService {

    private final CompraRepository compraRepository;
    private final ProveedorRepository proveedorRepository;
    private final UsuarioRepository usuarioRepository;
    private final ModelMapper modelMapper;
    private final ProductoRepository productoRepository;
    private final DetalleCompraRepository detalleCompraRepository;

    @Override
    public List<CompraDTO> listar() {
        return compraRepository.findAll().stream()
                .filter(compra -> !compra.isAnulado()) // Solo ventas no anuladas
                .map(compra -> {
                    CompraDTO dto = modelMapper.map(compra, CompraDTO.class);

                    if (compra.getProveedor() != null) {
                        dto.setIdProveedor(compra.getProveedor().getId());
                    }

                    if (compra.getUsuario() != null) {
                        dto.setIdUsuario(compra.getUsuario().getId());
                    }
                    List<DetalleCompraDTO> detalles = detalleCompraRepository.findByCompraId(compra.getId())
                            .stream()
                            .map(detalle -> {
                                Producto producto = detalle.getProducto();
                                return DetalleCompraDTO.builder()
                                        .id(detalle.getId())
                                        .cantidad(detalle.getCantidad())
                                        .precioUnitario(detalle.getPrecioUnitario())
                                        .subtotal(detalle.getPrecioUnitario() * detalle.getCantidad())
                                        .idCompra(compra.getId())
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
    public CompraDTO obtenerPorId(Long id) {
        Compra compra = compraRepository.findById(id)
                .filter(v -> !v.isAnulado())//solo si no esta anulada
                .orElseThrow(() -> new EntityNotFoundException("Compra no encontrada o anulada"));

        CompraDTO dto = modelMapper.map(compra, CompraDTO.class);

        if (compra.getProveedor() != null) {
            dto.setIdProveedor(compra.getProveedor().getId());
        }

        if (compra.getUsuario() != null) {
            dto.setIdUsuario(compra.getUsuario().getId());
        }

        List<DetalleCompraDTO> detalles = detalleCompraRepository.findByCompraId(compra.getId())
                .stream()
                .map( detalle -> {
                    Producto producto = detalle.getProducto();
                    return DetalleCompraDTO.builder()
                            .id(detalle.getId())
                            .cantidad(detalle.getCantidad())
                            .precioUnitario(detalle.getPrecioUnitario())
                            .subtotal(detalle.getPrecioUnitario() * detalle.getCantidad())
                            .idCompra(compra.getId())
                            .idProducto(producto.getId())
                            .nombreProducto(producto.getNombre())
                            .build();
                })
                .collect(Collectors.toList());

        dto.setDetalles(detalles);

        return dto;
    }

    @Override
    public CompraDTO registrar(CompraDTO dto, boolean forzarRegistro) {
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor())
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario())
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        BigDecimal totalCompra = BigDecimal.ZERO;

        // 🔹 Validar precios antes de guardar
        if (dto.getDetalles() != null) {
            for (DetalleCompraDTO det : dto.getDetalles()) {
                Producto producto = productoRepository.findById(det.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                BigDecimal precioProducto = BigDecimal.valueOf(producto.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal precioDetalle = BigDecimal.valueOf(det.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);

                if (!forzarRegistro && precioProducto.compareTo(precioDetalle) != 0) {
                    throw new PrecioDiferenteException(
                            "El precio del producto '" + producto.getNombre() +
                                    "' difiere del registrado. Actual: " + precioProducto +
                                    ", Nuevo: " + precioDetalle
                    );
                }
            }
        }

        // 🔹 Crear compra con fecha actual y total inicial 0
        Compra compra = Compra.builder()
                .fecha(LocalDateTime.now()) //  Fecha automática
                .total(BigDecimal.valueOf(0.0))
                .proveedor(proveedor)
                .usuario(usuario)
                .anulado(false)
                .build();

        compraRepository.save(compra);

        // 🔹 Guardar los detalles
        List<DetalleCompraDTO> detallesRespuesta = new ArrayList<>();
        if (dto.getDetalles() != null) {
            for (DetalleCompraDTO det : dto.getDetalles()) {
                Producto producto = productoRepository.findById(det.getIdProducto())
                        .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

                if (forzarRegistro) {
                    producto.setPrecioUnitario(det.getPrecioUnitario());
                }

                BigDecimal precioUnitario = BigDecimal.valueOf(det.getPrecioUnitario()).setScale(2, RoundingMode.HALF_UP);
                BigDecimal subtotal = precioUnitario.multiply(BigDecimal.valueOf(det.getCantidad())).setScale(2, RoundingMode.HALF_UP);

                totalCompra = totalCompra.add(subtotal);

                DetalleCompra detalle = DetalleCompra.builder()
                        .cantidad(det.getCantidad())
                        .precioUnitario(precioUnitario.doubleValue())
                        .subtotal(subtotal.doubleValue())
                        .producto(producto)
                        .compra(compra)
                        .build();

                detalleCompraRepository.save(detalle);

                // Actualizar stock
                producto.setStock(producto.getStock() + det.getCantidad());
                productoRepository.save(producto);

                // 📌 Preparar DTO de respuesta con IDs y subtotal
                DetalleCompraDTO detalleDTO = new DetalleCompraDTO();
                detalleDTO.setId(detalle.getId());
                detalleDTO.setCantidad(detalle.getCantidad());
                detalleDTO.setPrecioUnitario(detalle.getPrecioUnitario());
                detalleDTO.setSubtotal(detalle.getSubtotal());
                detalleDTO.setIdCompra(compra.getId());
                detalleDTO.setIdProducto(producto.getId());

                detallesRespuesta.add(detalleDTO);
            }
        }

        // 🔹 Actualizar el total de la compra
        compra.setTotal(BigDecimal.valueOf(totalCompra.doubleValue()));
        compraRepository.save(compra);

        // 📌 Armar respuesta final
        dto.setId(compra.getId());
        dto.setFecha(compra.getFecha());
        dto.setTotal(compra.getTotal());
        dto.setDetalles(detallesRespuesta);

        return dto;
    }





    @Override
    public CompraDTO actualizar(Long id, CompraDTO dto) {
        Compra compra = compraRepository.findById(id).orElseThrow();
        Proveedor proveedor = proveedorRepository.findById(dto.getIdProveedor()).orElseThrow();
        Usuario usuario = usuarioRepository.findById(dto.getIdUsuario()).orElseThrow();

        compra.setFecha(dto.getFecha());
        compra.setTotal(dto.getTotal());
        compra.setProveedor(proveedor);
        compra.setUsuario(usuario);

        compraRepository.save(compra);
        dto.setId(id);
        return dto;
    }

    public void anularCompra(Long idCompra) {
        Compra compra = compraRepository.findById(idCompra)
                .orElseThrow(() -> new RuntimeException("Compra no encontrada con ID: " + idCompra));

        if (compra.isAnulado()) {
            throw new RuntimeException("La compra ya está anulada");
        }

        compra.setAnulado(true);
        compraRepository.save(compra);

        // Ajustar stock: restar cantidades de productos comprados
        List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(idCompra);
        for (DetalleCompra detalle : detalles) {
            Producto producto = detalle.getProducto();
            producto.setStock(producto.getStock() - detalle.getCantidad());
            productoRepository.save(producto);
        }
    }
}
