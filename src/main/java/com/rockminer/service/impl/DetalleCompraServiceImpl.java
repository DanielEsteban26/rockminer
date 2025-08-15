package com.rockminer.service.impl;

import com.rockminer.dto.DetalleCompraDTO;
import com.rockminer.entity.Compra;
import com.rockminer.entity.DetalleCompra;
import com.rockminer.entity.Producto;
import com.rockminer.repository.CompraRepository;
import com.rockminer.repository.DetalleCompraRepository;
import com.rockminer.repository.ProductoRepository;
import com.rockminer.service.DetalleCompraService;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class DetalleCompraServiceImpl implements DetalleCompraService {

    private final DetalleCompraRepository detalleCompraRepository;
    private final ProductoRepository productoRepository;
    private final CompraRepository compraRepository;
    private final ModelMapper modelMapper;

    @Override
    public List<DetalleCompraDTO> listar() {
        return detalleCompraRepository.findAll().stream().map(detalle -> {
            DetalleCompraDTO dto = modelMapper.map(detalle, DetalleCompraDTO.class);
            dto.setIdProducto(detalle.getProducto().getId());
            dto.setIdCompra(detalle.getCompra().getId());
            return dto;
        }).collect(Collectors.toList());
    }

    @Override
    public List<DetalleCompraDTO> listarPorIdCompra(Long idCompra) {
        List<DetalleCompra> detalles = detalleCompraRepository.findByCompraId(idCompra);

        return detalles.stream()
                .map(detalle -> {
                    DetalleCompraDTO dto = modelMapper.map(detalle, DetalleCompraDTO.class);
                    dto.setIdCompra(detalle.getCompra().getId());
                    dto.setIdProducto(detalle.getProducto().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }


    @Override
    public DetalleCompraDTO obtenerPorId(Long id) {
        DetalleCompra detalle = detalleCompraRepository.findById(id).orElseThrow();
        DetalleCompraDTO dto = modelMapper.map(detalle, DetalleCompraDTO.class);
        dto.setIdProducto(detalle.getProducto().getId());
        dto.setIdCompra(detalle.getCompra().getId());
        return dto;
    }

    @Override
    public DetalleCompraDTO registrar(DetalleCompraDTO dto) {
        Producto producto = productoRepository.findById(dto.getIdProducto()).orElseThrow();
        Compra compra = compraRepository.findById(dto.getIdCompra()).orElseThrow();

        DetalleCompra detalle = DetalleCompra.builder()
                .cantidad(dto.getCantidad())
                .precioUnitario(dto.getPrecioUnitario())
                .subtotal(dto.getCantidad() * dto.getPrecioUnitario())
                .producto(producto)
                .compra(compra)
                .build();

        detalleCompraRepository.save(detalle);

        // Aumentar stock
        producto.setStock(producto.getStock() + dto.getCantidad());
        productoRepository.save(producto);

        dto.setId(detalle.getId());
        dto.setSubtotal(detalle.getSubtotal());
        return dto;
    }


    @Override
    public DetalleCompraDTO actualizar(Long id, DetalleCompraDTO dto) {
        DetalleCompra existente = detalleCompraRepository.findById(id).orElseThrow();
        Producto producto = productoRepository.findById(dto.getIdProducto()).orElseThrow();
        Compra compra = compraRepository.findById(dto.getIdCompra()).orElseThrow();

        int stockOriginal = producto.getStock();
        int cantidadAnterior = existente.getCantidad();
        int nuevaCantidad = dto.getCantidad();

        // Revertir la cantidad anterior
        producto.setStock(stockOriginal - cantidadAnterior);

        // Sumar la nueva cantidad
        producto.setStock(producto.getStock() + nuevaCantidad);
        productoRepository.save(producto);

        existente.setCantidad(nuevaCantidad);
        existente.setPrecioUnitario(dto.getPrecioUnitario());
        existente.setSubtotal(nuevaCantidad * dto.getPrecioUnitario());
        existente.setProducto(producto);
        existente.setCompra(compra);

        detalleCompraRepository.save(existente);

        dto.setId(id);
        dto.setSubtotal(existente.getSubtotal());
        return dto;
    }

    @Override
    public void eliminar(Long id) {
        detalleCompraRepository.deleteById(id);
    }
}
