package com.rockminer.service;

import com.rockminer.dto.CompraDTO;

import java.util.List;

public interface CompraService {
    List<CompraDTO> listar();
    CompraDTO obtenerPorId(Long id);
    CompraDTO registrar(CompraDTO dto, boolean forzarRegistro);
    CompraDTO actualizar(Long id, CompraDTO dto);
    void anularCompra(Long id);
}
