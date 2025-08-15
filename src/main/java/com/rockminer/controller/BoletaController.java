package com.rockminer.controller;

import com.rockminer.service.BoletaService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;


@RestController
@RequestMapping("/api/boletas")
@RequiredArgsConstructor
public class BoletaController {

    private final BoletaService boletaService;

    @GetMapping("/venta/{id}")
    public ResponseEntity<byte[]> generarBoleta(@PathVariable Long id) {
        try {
            byte[] pdfBytes = boletaService.generarBoletaPDF(id).readAllBytes();

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_PDF);
            headers.setContentDisposition(ContentDisposition
                    .inline()
                    .filename("boleta_" + id + ".pdf")
                    .build());

            return ResponseEntity.ok()
                    .headers(headers)
                    .body(pdfBytes);

        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(null);
        }
    }

    @GetMapping("/test")
    public String test() {
        System.out.println("✅ Entró a /api/venta/test");
        return "OK";
    }
}
