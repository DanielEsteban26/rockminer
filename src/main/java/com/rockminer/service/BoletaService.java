package com.rockminer.service;

import java.io.ByteArrayInputStream;

public interface BoletaService {
    ByteArrayInputStream generarBoletaPDF(Long ventaId);
}
