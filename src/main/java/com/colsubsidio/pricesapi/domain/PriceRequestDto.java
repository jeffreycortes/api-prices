package com.colsubsidio.pricesapi.domain;

import lombok.Data;

@Data
public class PriceRequestDto {
    String fechaAplicacion;
    String productoId;
    String cadenaId;
}
