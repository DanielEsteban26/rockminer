package com.rockminer.repository;

import com.rockminer.dto.ReporteVentaDTO;
import com.rockminer.entity.Venta;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;


import java.time.LocalDateTime;
import java.util.List;

public interface VentaRepository extends JpaRepository<Venta, Long> {
    @Query("SELECT new com.rockminer.dto.ReporteVentaDTO(v.id, v.fecha, v.total, u.nombre) " +
            "FROM Venta v JOIN v.usuario u " +
            "WHERE v.fecha BETWEEN :fechaInicio AND :fechaFin")
    List<ReporteVentaDTO> findByFechaBetween(@Param("fechaInicio") LocalDateTime fechaInicio,
                                             @Param("fechaFin") LocalDateTime fechaFin);

    @Query("SELECT COUNT(v) FROM Venta v WHERE v.anulado = false")
    Long countVentasActivas();

    @Query("SELECT COUNT(v) FROM Venta v WHERE v.anulado = true")
    Long countVentasAnuladas();
}
