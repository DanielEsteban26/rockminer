package com.rockminer.service.impl;

import com.rockminer.dto.ProveedorDTO;
import com.rockminer.entity.Proveedor;
import com.rockminer.repository.ProveedorRepository;
import com.rockminer.service.ProveedorService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ProveedorServiceImpl implements ProveedorService {

    @Autowired
    private ProveedorRepository proveedorRepository;

    @Autowired
    private ModelMapper modelMapper;

    @Override
    public List<ProveedorDTO> listarProveedores() {
        return proveedorRepository.findAll().stream()
                .map(proveedor -> modelMapper.map(proveedor, ProveedorDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public ProveedorDTO obtenerProveedorPorId(Long id) {
        Proveedor proveedor = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));
        return modelMapper.map(proveedor, ProveedorDTO.class);
    }

    @Override
    public ProveedorDTO registrarProveedor(ProveedorDTO dto) {
        Proveedor nuevo = modelMapper.map(dto, Proveedor.class);
        return modelMapper.map(proveedorRepository.save(nuevo), ProveedorDTO.class);
    }

    @Override
    public ProveedorDTO actualizarProveedor(Long id, ProveedorDTO dto) {
        Proveedor existente = proveedorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Proveedor no encontrado"));

        existente.setRazonSocial(dto.getRazonSocial());
        existente.setRuc(dto.getRuc());
        existente.setTelefono(dto.getTelefono());
        existente.setDireccion(dto.getDireccion());

        return modelMapper.map(proveedorRepository.save(existente), ProveedorDTO.class);
    }

    @Override
    public void eliminarProveedor(Long id) {
        proveedorRepository.deleteById(id);
    }
}
