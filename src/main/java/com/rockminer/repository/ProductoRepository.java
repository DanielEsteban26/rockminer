package com.rockminer.repository;

import com.rockminer.dto.ProductoStockDTO;
import com.rockminer.entity.Producto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface ProductoRepository extends JpaRepository<Producto, Long> {
    @Query("SELECT new com.rockminer.dto.ProductoStockDTO(p.id, p.nombre, p.descripcion, p.stock) FROM Producto p")
    List<ProductoStockDTO> listarStockProductos();
}
