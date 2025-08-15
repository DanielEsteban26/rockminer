package com.rockminer.service;

import com.rockminer.dto.UsuarioDTO;

import java.util.List;

public interface UsuarioService {
    List<UsuarioDTO> listarUsuarios();
    UsuarioDTO obtenerUsuarioPorId(Long id);
    UsuarioDTO registrarUsuario(UsuarioDTO dto);
    UsuarioDTO actualizarUsuario(Long id, UsuarioDTO dto);
    void eliminarUsuario(Long id);
}
