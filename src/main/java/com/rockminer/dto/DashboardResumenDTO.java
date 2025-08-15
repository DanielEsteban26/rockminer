package com.rockminer.dto;

import lombok.*;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class DashboardResumenDTO {
    private Long totalUsuarios;
    private Long totalProductos;
    private Long totalProveedores;
    private Long totalCompras;
    private Long totalVentas;
    private Long ventasActivas;
    private Long ventasAnuladas;
    private long comprasActivas;
    private long comprasAnuladas;
}
