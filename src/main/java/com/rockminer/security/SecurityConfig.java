package com.rockminer.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Autowired
    private JwtFilter jwtFilter;

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        return http
                .csrf(csrf -> csrf.disable())
                .authorizeHttpRequests(auth -> auth

                        // Endpoint de login público
                        .requestMatchers("/api/auth/**").permitAll()

                        // ADMIN + SUPERVISOR: acceso a dashboard
                        .requestMatchers("/api/dashboard/**").hasAnyRole("ADMIN", "SUPERVISOR")

                        // ADMIN: acceso completo a usuarios y reportes
                        .requestMatchers(
                                "/api/usuarios/**",
                                "/api/reportes/**"
                        ).hasRole("ADMIN")

                        // ADMIN + ALMACENERO: productos, categorías, inventario, compras, detalle compras, proveedores
                        .requestMatchers(
                                "/api/productos/**",
                                "/api/categorias/**",
                                "/api/inventario/**",
                                "/api/compras/**",
                                "/api/detalle-compras/**",
                                "/api/proveedores/**"
                        ).hasAnyRole("ADMIN", "ALMACENERO")

                        // ADMIN + VENDEDOR: ventas, detalle ventas
                        .requestMatchers(
                                "/api/ventas/**",
                                "/api/detalle-ventas/**",
                                "/api/boletas/**"
                        ).hasAnyRole("ADMIN", "VENDEDOR")

                        // Cualquier otra ruta requiere autenticación
                        .anyRequest().authenticated()
                )
                .addFilterBefore(jwtFilter, UsernamePasswordAuthenticationFilter.class)
                .build();
    }


    @Bean
    public AuthenticationManager authManager(AuthenticationConfiguration config) throws Exception {
        return config.getAuthenticationManager();
    }

    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}
