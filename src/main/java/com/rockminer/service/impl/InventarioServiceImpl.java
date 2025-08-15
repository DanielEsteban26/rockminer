package com.rockminer.service.impl;

import com.rockminer.dto.ProductoStockDTO;
import com.rockminer.repository.ProductoRepository;
import com.rockminer.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InventarioServiceImpl implements InventarioService {

    @Autowired
    private ProductoRepository productoRepository;

    @Override
    public List<ProductoStockDTO> listarStock() {
        return productoRepository.listarStockProductos();
    }
}
