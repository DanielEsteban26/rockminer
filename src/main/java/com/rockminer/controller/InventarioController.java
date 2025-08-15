package com.rockminer.controller;

import com.rockminer.dto.ProductoStockDTO;
import com.rockminer.service.InventarioService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/inventario")
@CrossOrigin(origins = "*")
public class InventarioController {

    @Autowired
    private InventarioService inventarioService;

    @GetMapping("/stock-productos")
    public List<ProductoStockDTO> listarStock() {
        return inventarioService.listarStock();
    }
}
