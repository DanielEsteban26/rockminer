package com.rockminer.service.impl;

import com.rockminer.dto.DashboardResumenDTO;
import com.rockminer.repository.*;
import com.rockminer.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class DashboardServiceImpl implements DashboardService {

    @Autowired
    private UsuarioRepository usuarioRepository;
    @Autowired
    private ProductoRepository productoRepository;
    @Autowired
    private ProveedorRepository proveedorRepository;
    @Autowired
    private CompraRepository compraRepository;
    @Autowired
    private VentaRepository ventaRepository;

    @Override
    public DashboardResumenDTO obtenerResumen() {
        return DashboardResumenDTO.builder()
                .totalUsuarios(usuarioRepository.count())
                .totalProductos(productoRepository.count())
                .totalProveedores(proveedorRepository.count())
                .totalCompras(compraRepository.count())
                .totalVentas(ventaRepository.count())
                .ventasActivas(ventaRepository.countVentasActivas())
                .ventasAnuladas(ventaRepository.countVentasAnuladas())
                .comprasActivas(compraRepository.countComprasActivas())
                .comprasAnuladas(compraRepository.countComprasAnuladas())
                .build();
    }
}
