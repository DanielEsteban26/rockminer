package com.rockminer.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DetalleCompraDTO {

    private Long id;
    private Integer cantidad;
    private Double precioUnitario;
    private Double subtotal;
    private Long idCompra;
    private Long idProducto;
    private String nombreProducto;
}
