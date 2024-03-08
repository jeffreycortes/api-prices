package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceResponseDto {
    String productoId;
    String cadenaId;
    String tarifa;
    String fechasAplicacion;
    String precioFinal;
}
