package com.rockminer.service;

import com.rockminer.dto.ReporteVentaDTO;

import java.time.LocalDateTime;
import java.util.List;

public interface ReporteService {
    List<ReporteVentaDTO> obtenerVentasPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin);
}
