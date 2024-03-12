package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class PriceRequestDto {
    String fechaAplicacion;
    String productoId;
    String cadenaId;
}
