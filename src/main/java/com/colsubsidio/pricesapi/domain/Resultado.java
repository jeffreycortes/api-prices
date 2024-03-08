package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Resultado<T> {
    private String codigo;
    private boolean exitoso;
    private Error error;
    private Transaccion<T> transaccion;
}
