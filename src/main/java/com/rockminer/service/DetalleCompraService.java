package com.rockminer.service;

import com.rockminer.dto.DetalleCompraDTO;

import java.util.List;

public interface DetalleCompraService {
    List<DetalleCompraDTO> listar();
    List<DetalleCompraDTO> listarPorIdCompra(Long idCompra);
    DetalleCompraDTO obtenerPorId(Long id);
    DetalleCompraDTO registrar(DetalleCompraDTO dto);
    DetalleCompraDTO actualizar(Long id, DetalleCompraDTO dto);
    void eliminar(Long id);
}
