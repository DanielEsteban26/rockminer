package com.rockminer.service.impl;

import com.rockminer.dto.ReporteVentaDTO;
import com.rockminer.repository.VentaRepository;
import com.rockminer.service.ReporteService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ReporteServiceImpl implements ReporteService {

    private final VentaRepository ventaRepository;

    @Override
    public List<ReporteVentaDTO> obtenerVentasPorRango(LocalDateTime fechaInicio, LocalDateTime fechaFin) {
        return ventaRepository.findByFechaBetween(fechaInicio, fechaFin);
    }
}
