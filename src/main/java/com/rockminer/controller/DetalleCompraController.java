package com.rockminer.controller;

import com.rockminer.dto.DetalleCompraDTO;
import com.rockminer.service.DetalleCompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-compras")
@RequiredArgsConstructor
public class DetalleCompraController {

    private final DetalleCompraService detalleCompraService;

    @GetMapping
    public List<DetalleCompraDTO> listar() {
        return detalleCompraService.listar();
    }

    @GetMapping("/compra/{idCompra}")
    public List<DetalleCompraDTO> listarPorCompra(@PathVariable Long idCompra) {
        return detalleCompraService.listarPorIdCompra(idCompra);
    }

    @GetMapping("/{id}")
    public DetalleCompraDTO obtener(@PathVariable Long id) {
        return detalleCompraService.obtenerPorId(id);
    }

    @PostMapping
    public DetalleCompraDTO registrar(@RequestBody DetalleCompraDTO dto) {
        return detalleCompraService.registrar(dto);
    }

    @PutMapping("/{id}")
    public DetalleCompraDTO actualizar(@PathVariable Long id, @RequestBody DetalleCompraDTO dto) {
        return detalleCompraService.actualizar(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        detalleCompraService.eliminar(id);
    }
}
