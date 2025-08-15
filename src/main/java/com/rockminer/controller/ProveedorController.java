package com.rockminer.controller;

import com.rockminer.dto.ProveedorDTO;
import com.rockminer.service.ProveedorService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/proveedores")
public class ProveedorController {

    @Autowired
    private ProveedorService proveedorService;

    @GetMapping
    public List<ProveedorDTO> listar() {
        return proveedorService.listarProveedores();
    }

    @GetMapping("/{id}")
    public ProveedorDTO obtener(@PathVariable Long id) {
        return proveedorService.obtenerProveedorPorId(id);
    }

    @PostMapping
    public ProveedorDTO registrar(@RequestBody ProveedorDTO dto) {
        return proveedorService.registrarProveedor(dto);
    }

    @PutMapping("/{id}")
    public ProveedorDTO actualizar(@PathVariable Long id, @RequestBody ProveedorDTO dto) {
        return proveedorService.actualizarProveedor(id, dto);
    }

    @DeleteMapping("/{id}")
    public void eliminar(@PathVariable Long id) {
        proveedorService.eliminarProveedor(id);
    }
}
