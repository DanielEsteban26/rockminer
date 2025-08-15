package com.rockminer.repository;

import com.rockminer.entity.Compra;
import com.rockminer.entity.Usuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface CompraRepository extends JpaRepository<Compra, Long> {
    List<Compra> findByUsuario(Usuario usuario);

    @Query("SELECT COUNT(v) FROM Compra v WHERE v.anulado = false")
    Long countComprasActivas();

    @Query("SELECT COUNT(v) FROM Compra v WHERE v.anulado = true")
    Long countComprasAnuladas();

}
