package com.rockminer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class DetalleVentaDTO {
    private Long id;
    private int cantidad;
    private double precioUnitario;
    private double subtotal;
    private Long idVenta; //opcional para registrar venta
    private Long idProducto;
    private String nombreProducto; // <-- NUEVO CAMPO
}
