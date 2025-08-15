package com.rockminer.controller;

import com.rockminer.dto.DashboardResumenDTO;
import com.rockminer.service.DashboardService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/dashboard")
public class DashboardController {

    @Autowired
    private DashboardService dashboardService;

    @GetMapping("/resumen")
    public DashboardResumenDTO obtenerResumen() {
        return dashboardService.obtenerResumen();
    }
}
