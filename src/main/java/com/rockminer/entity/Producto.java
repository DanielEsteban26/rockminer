package com.rockminer.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "productos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Producto {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nombre;

    private String descripcion;

    private String tipoProducto; // broca, jumbo, etc.

    private Integer stock;

    private Double precioUnitario;

    private Boolean estado = true;

    @ManyToOne
    @JoinColumn(name = "id_categoria")
    private Categoria categoria;
}
