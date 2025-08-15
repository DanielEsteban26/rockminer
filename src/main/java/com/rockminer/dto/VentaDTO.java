package com.rockminer.dto;

import lombok.*;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class VentaDTO {
    private Long id;
    private LocalDateTime fecha;
    private Double total; // evitar enviar al registrar ya que eso se calcula solo
    private boolean anulado;
    private Long idUsuario;
    private List<DetalleVentaDTO> detalles;
}
