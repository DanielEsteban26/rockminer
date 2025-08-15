package com.rockminer.controller;

import com.rockminer.dto.VentaDTO;
import com.rockminer.service.VentaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/ventas")
@RequiredArgsConstructor
public class VentaController {

    private final VentaService ventaService;

    // Listar todas las ventas
    @GetMapping
    public ResponseEntity<List<VentaDTO>> listar() {
        return ResponseEntity.ok(ventaService.listar());
    }

    // Obtener una venta por ID
    @GetMapping("/{id}")
    public ResponseEntity<VentaDTO> obtener(@PathVariable Long id) {
        return ResponseEntity.ok(ventaService.obtenerPorId(id));
    }

    // Registrar una nueva venta
    @PostMapping
    public ResponseEntity<VentaDTO> registrar(@RequestBody VentaDTO dto) {
        VentaDTO nuevaVenta = ventaService.registrar(dto);
        return ResponseEntity.ok(nuevaVenta); // Se devuelve la venta creada al frontend
    }

    // Actualizar una venta existente
    @PutMapping("/{id}")
    public ResponseEntity<VentaDTO> actualizar(@PathVariable Long id, @RequestBody VentaDTO dto) {
        VentaDTO actualizada = ventaService.actualizar(id, dto);
        return ResponseEntity.ok(actualizada);
    }

    // Anular una venta (soft delete)
    @PutMapping("/{id}/anular")
    public ResponseEntity<Void> anularVenta(@PathVariable Long id) {
        ventaService.anularVenta(id);
        return ResponseEntity.noContent().build(); // 204: éxito sin contenido
    }
}
