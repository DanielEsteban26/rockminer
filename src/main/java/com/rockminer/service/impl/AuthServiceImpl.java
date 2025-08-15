package com.rockminer.service.impl;

import com.rockminer.dto.LoginRequest;
import com.rockminer.dto.LoginResponse;
import com.rockminer.entity.Usuario;
import com.rockminer.repository.UsuarioRepository;
import com.rockminer.security.JwtUtil;
import com.rockminer.service.AuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.*;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UsuarioRepository usuarioRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Override
    public LoginResponse login(LoginRequest request) {
        UsernamePasswordAuthenticationToken authToken =
                new UsernamePasswordAuthenticationToken(request.getUsuario(), request.getClave());

        authManager.authenticate(authToken);

        String token = jwtUtil.generarToken(request.getUsuario());
        return new LoginResponse(token);
    }
}
