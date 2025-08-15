package com.rockminer.service;

import com.rockminer.dto.ProveedorDTO;

import java.util.List;

public interface ProveedorService {
    List<ProveedorDTO> listarProveedores();
    ProveedorDTO obtenerProveedorPorId(Long id);
    ProveedorDTO registrarProveedor(ProveedorDTO dto);
    ProveedorDTO actualizarProveedor(Long id, ProveedorDTO dto);
    void eliminarProveedor(Long id);
}
