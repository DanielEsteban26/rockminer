package com.rockminer.service;

import com.rockminer.dto.ProductoStockDTO;
import java.util.List;

public interface InventarioService {
    List<ProductoStockDTO> listarStock();
}
