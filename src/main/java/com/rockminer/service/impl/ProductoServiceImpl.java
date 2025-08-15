package com.rockminer.service.impl;

import com.rockminer.dto.ProductoDTO;
import com.rockminer.entity.Categoria;
import com.rockminer.entity.Producto;
import com.rockminer.repository.CategoriaRepository;
import com.rockminer.repository.ProductoRepository;
import com.rockminer.service.ProductoService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProductoServiceImpl implements ProductoService {

    @Autowired
    private ProductoRepository productoRepository;

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProductoDTO> listar() {
        return productoRepository.findAll().stream()
                .map(p -> {
                    ProductoDTO dto = modelMapper.map(p, ProductoDTO.class);
                    dto.setIdCategoria(p.getCategoria().getId().intValue());
                    return dto;
                }).collect(Collectors.toList());
    }

    @Override
    public ProductoDTO obtenerPorId(Long id) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));
        ProductoDTO dto = modelMapper.map(producto, ProductoDTO.class);
        dto.setIdCategoria(producto.getCategoria().getId().intValue());
        return dto;
    }

    @Override
    public ProductoDTO registrar(ProductoDTO dto) {
        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria().longValue())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        Producto nuevo = modelMapper.map(dto, Producto.class);
        nuevo.setCategoria(categoria);

        return modelMapper.map(productoRepository.save(nuevo), ProductoDTO.class);
    }

    @Override
    public ProductoDTO actualizar(Long id, ProductoDTO dto) {
        Producto producto = productoRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado"));

        Categoria categoria = categoriaRepository.findById(dto.getIdCategoria().longValue())
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        producto.setNombre(dto.getNombre());
        producto.setDescripcion(dto.getDescripcion());
        producto.setTipoProducto(dto.getTipoProducto());
        producto.setStock(dto.getStock());
        producto.setPrecioUnitario(dto.getPrecioUnitario());
        producto.setEstado(dto.getEstado());
        producto.setCategoria(categoria);

        return modelMapper.map(productoRepository.save(producto), ProductoDTO.class);
    }

    @Override
    public void eliminar(Long id) {
        if (!productoRepository.existsById(id)) {
            throw new RuntimeException("Producto no encontrado");
        }
        productoRepository.deleteById(id);
    }

    @Override
    public void ajustarStock(Long idProducto, Integer nuevoStock) {
        Producto producto = productoRepository.findById(idProducto)
                .orElseThrow(() -> new RuntimeException("Producto no encontrado con ID: " + idProducto));

        producto.setStock(nuevoStock);
        productoRepository.save(producto);
    }
}
