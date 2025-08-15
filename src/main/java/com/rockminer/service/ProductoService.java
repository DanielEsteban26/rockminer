package com.rockminer.service;

import com.rockminer.dto.ProductoDTO;
import com.rockminer.entity.Producto;

import java.util.List;

public interface ProductoService {
    List<ProductoDTO> listar();
    ProductoDTO obtenerPorId(Long id);
    ProductoDTO registrar(ProductoDTO dto);
    ProductoDTO actualizar(Long id, ProductoDTO dto);
    void eliminar(Long id);
    void ajustarStock(Long idProducto, Integer nuevoStock);
}

