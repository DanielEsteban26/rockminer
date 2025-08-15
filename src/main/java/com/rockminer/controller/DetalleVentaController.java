package com.rockminer.controller;

import com.rockminer.dto.DetalleVentaDTO;
import com.rockminer.service.DetalleVentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/detalle-ventas")
@RequiredArgsConstructor
public class DetalleVentaController {

    private final DetalleVentaService detalleVentaService;

    @GetMapping("/venta/{idVenta}")
    public List<DetalleVentaDTO> listarPorVenta(@PathVariable Long idVenta) {
        return detalleVentaService.listarPorIdVenta(idVenta);
    }

    @PostMapping
    public DetalleVentaDTO registrar(@RequestBody DetalleVentaDTO dto) {
        return detalleVentaService.registrar(dto);
    }
}
