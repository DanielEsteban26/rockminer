package com.rockminer.controller;

import com.rockminer.dto.AjusteStockDTO;
import com.rockminer.dto.ProductoDTO;
import com.rockminer.service.ProductoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/productos")
public class ProductoController {

    @Autowired
    private ProductoService productoService;

    @GetMapping
    public List<ProductoDTO> listar() {
        return productoService.listar();
    }

    @GetMapping("/{id}")
    public ProductoDTO obtener(@PathVariable Long id) {
        return productoService.obtenerPorId(id);
    }

    @PostMapping
    public ProductoDTO registrar(@RequestBody ProductoDTO dto) {
        return productoService.registrar(dto);
    }

    @PutMapping("/{id}")
    public ProductoDTO actualizar(@PathVariable Long id, @RequestBody ProductoDTO dto) {
        return productoService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        productoService.eliminar(id);
    }


    @PutMapping("/inventario/{idProducto}")
    public void ajustarStock(@PathVariable Long idProducto, @RequestBody AjusteStockDTO dto) {
        productoService.ajustarStock(idProducto, dto.getNuevoStock());
    }


}
