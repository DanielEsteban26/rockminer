package com.rockminer.dto;

import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ReporteVentaDTO {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private String nombreUsuario;

    public ReporteVentaDTO(Long id, LocalDateTime fecha, Number total, String nombreUsuario) {
        this.id = id;
        this.fecha = fecha;
        this.total = BigDecimal.valueOf(total.doubleValue());
        this.nombreUsuario = nombreUsuario;
    }
}
