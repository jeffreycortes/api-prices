package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.Date;

@Data
@Builder
public class PriceRequestDto {
    Date fechaAplicacion;
    long productoId;
    long cadenaId;
}
