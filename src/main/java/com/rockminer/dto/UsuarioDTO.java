package com.rockminer.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UsuarioDTO {
    private Long id;
    private String nombre;
    private String usuario;
    private String clave;
    private String rol;
    private Boolean estado;
}
