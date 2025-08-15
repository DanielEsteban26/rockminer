package com.rockminer.dto;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductoDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private String tipoProducto;
    private Integer stock;
    private Double precioUnitario;
    private Boolean estado;
    private Integer idCategoria;
}
