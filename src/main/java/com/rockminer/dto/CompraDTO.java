package com.rockminer.dto;

import jakarta.persistence.PrePersist;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CompraDTO {
    private Long id;
    private LocalDateTime fecha;
    private BigDecimal total;
    private Long idProveedor;
    private Long idUsuario;
    private boolean anulado;
    private List<DetalleCompraDTO> detalles;

}
