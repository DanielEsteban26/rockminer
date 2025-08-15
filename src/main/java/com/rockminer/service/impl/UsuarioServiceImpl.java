package com.rockminer.service.impl;

import com.rockminer.dto.UsuarioDTO;
import com.rockminer.entity.Usuario;
import com.rockminer.repository.UsuarioRepository;
import com.rockminer.service.UsuarioService;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class UsuarioServiceImpl implements UsuarioService {

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private ModelMapper modelMapper;
    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public List<UsuarioDTO> listarUsuarios() {
        return usuarioRepository.findAll().stream()
                .map(u -> modelMapper.map(u, UsuarioDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public UsuarioDTO obtenerUsuarioPorId(Long id) {
        Usuario usuario = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));
        return modelMapper.map(usuario, UsuarioDTO.class);
    }

    @Override
    public UsuarioDTO registrarUsuario(UsuarioDTO dto) {
        Usuario nuevo = modelMapper.map(dto, Usuario.class);

        // Encriptar clave
        nuevo.setClave(passwordEncoder.encode(dto.getClave()));

        return modelMapper.map(usuarioRepository.save(nuevo), UsuarioDTO.class);
    }


    @Override
    public UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto) {
        Usuario existente = usuarioRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Usuario no encontrado"));

        existente.setNombre(dto.getNombre());
        existente.setUsuario(dto.getUsuario());
        existente.setRol(dto.getRol());
        existente.setEstado(dto.getEstado());

        // Si el DTO trae una nueva clave, la encriptamos y actualizamos
        if (dto.getClave() != null && !dto.getClave().isEmpty()) {
            existente.setClave(passwordEncoder.encode(dto.getClave()));
        }

        return modelMapper.map(usuarioRepository.save(existente), UsuarioDTO.class);
    }

    @Override
    public void eliminarUsuario(Long id) {
        if (!usuarioRepository.existsById(id)) {
            throw new RuntimeException("Usuario no encontrado");
        }
        usuarioRepository.deleteById(id);
    }
}
