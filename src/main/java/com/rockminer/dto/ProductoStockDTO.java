package com.rockminer.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ProductoStockDTO {
    private Long id;
    private String nombre;
    private String descripcion;
    private int stock;
}
