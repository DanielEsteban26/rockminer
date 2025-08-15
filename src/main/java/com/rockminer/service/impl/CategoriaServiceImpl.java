package com.rockminer.service.impl;

import com.rockminer.dto.CategoriaDTO;
import com.rockminer.entity.Categoria;
import com.rockminer.repository.CategoriaRepository;
import com.rockminer.service.CategoriaService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class CategoriaServiceImpl implements CategoriaService {

    @Autowired
    private CategoriaRepository categoriaRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<CategoriaDTO> listarCategorias() {
        return categoriaRepository.findAll().stream()
                .map(c -> modelMapper.map(c, CategoriaDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public CategoriaDTO obtenerCategoriaPorId(Long id) {
        Categoria categoria = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));
        return modelMapper.map(categoria, CategoriaDTO.class);
    }

    @Override
    public CategoriaDTO registrarCategoria(CategoriaDTO dto) {
        Categoria nueva = modelMapper.map(dto, Categoria.class);
        return modelMapper.map(categoriaRepository.save(nueva), CategoriaDTO.class);
    }

    @Override
    public CategoriaDTO actualizarCategoria(Long id, CategoriaDTO dto) {
        Categoria existente = categoriaRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Categoría no encontrada"));

        existente.setNombre(dto.getNombre());
        existente.setDescripcion(dto.getDescripcion());

        return modelMapper.map(categoriaRepository.save(existente), CategoriaDTO.class);
    }

    @Override
    public void eliminarCategoria(Long id) {
        if (!categoriaRepository.existsById(id)) {
            throw new RuntimeException("Categoría no encontrada");
        }
        categoriaRepository.deleteById(id);
    }
}
