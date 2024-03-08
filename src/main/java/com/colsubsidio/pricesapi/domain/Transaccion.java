package com.colsubsidio.pricesapi.domain;

import com.colsubsidio.pricesapi.tmp.domain.CuentaUsuarioEmpresaResDto;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Transaccion<T> {
    private String codigo;
    private String mensaje;
    private T data;
}
