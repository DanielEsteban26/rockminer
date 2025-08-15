package com.rockminer.service;

import com.rockminer.dto.DetalleVentaDTO;

import java.util.List;

public interface DetalleVentaService {
    List<DetalleVentaDTO> listarPorIdVenta(Long idVenta);
    DetalleVentaDTO registrar(DetalleVentaDTO dto);
}
