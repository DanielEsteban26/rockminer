package com.rockminer.service;

import com.rockminer.dto.CategoriaDTO;

import java.util.List;

public interface CategoriaService {
    List<CategoriaDTO> listarCategorias();
    CategoriaDTO obtenerCategoriaPorId(Long id);
    CategoriaDTO registrarCategoria(CategoriaDTO dto);
    CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto);
    void eliminarCategoria(Long id);
}
