package com.rockminer.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT) // 409 Conflict
public class PrecioDiferenteException extends RuntimeException {
    public PrecioDiferenteException(String mensaje) {
        super(mensaje);
    }
}
