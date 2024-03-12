package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.math.BigDecimal;
import java.util.Date;

@Data
@Builder
public class PriceResponseDto {
    long productoId;
    long cadenaId;
    int tarifa;
    String fechasAplicacion;
    BigDecimal precioFinal;
    String moneda;
}
