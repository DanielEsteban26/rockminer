package com.rockminer.controller;

import com.rockminer.dto.CompraDTO;
import com.rockminer.exception.PrecioDiferenteException;
import com.rockminer.service.CompraService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/compras")
@RequiredArgsConstructor
public class CompraController {

    private final CompraService compraService;

    @GetMapping
    public ResponseEntity<List<CompraDTO>> listar() {
        return ResponseEntity.ok(compraService.listar());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CompraDTO> obtenerPorId(@PathVariable Long id) {
        return ResponseEntity.ok(compraService.obtenerPorId(id));
    }

    @PostMapping
    public ResponseEntity<?> registrarCompra(@RequestBody CompraDTO dto,
                                             @RequestParam(defaultValue = "false") boolean forzarRegistro) {
        try {
            CompraDTO compra = compraService.registrar(dto, forzarRegistro);
            return ResponseEntity.status(HttpStatus.CREATED).body(compra);
        } catch (PrecioDiferenteException ex) {
            return ResponseEntity.status(HttpStatus.CONFLICT)
                    .body(Map.of(
                            "mensaje", ex.getMessage(),
                            "requiereConfirmacion", true
                    ));
        }
    }

    @PutMapping("/{id}")
    public ResponseEntity<CompraDTO> actualizar(@PathVariable Long id, @RequestBody CompraDTO dto) {
        return ResponseEntity.ok(compraService.actualizar(id, dto));
    }

    @PutMapping("/anular/{id}")
    public ResponseEntity<String> anularCompra(@PathVariable Long id) {
        compraService.anularCompra(id);
        return ResponseEntity.ok("Compra anulada correctamente");
    }
}
