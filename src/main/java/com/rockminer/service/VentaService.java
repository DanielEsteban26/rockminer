package com.rockminer.service;

import com.rockminer.dto.VentaDTO;

import java.util.List;

public interface VentaService {
    List<VentaDTO> listar();
    VentaDTO obtenerPorId(Long id);
    VentaDTO registrar(VentaDTO ventaDTO);
    VentaDTO actualizar(Long id, VentaDTO ventaDTO);
    void anularVenta(Long idVenta);
}

