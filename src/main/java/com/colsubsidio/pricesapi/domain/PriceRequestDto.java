package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PriceRequestDto {
    long cadenaId;
    long productoId;
    Date fechaAplicacion;

    public static PriceRequestDto instance(long cadenaId,
                                           long productoId,
                                           Date fechaAplicacion) {
        return new PriceRequestDto(cadenaId, productoId, fechaAplicacion);
    }
}
