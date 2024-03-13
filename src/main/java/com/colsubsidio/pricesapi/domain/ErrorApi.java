package com.colsubsidio.pricesapi.domain;

import lombok.Builder;
import lombok.Data;

import java.util.ArrayList;
import java.util.List;

@Data
@Builder
public class ErrorApi {
    private String codigo;
    private String descripcion;
    @Builder.Default
    private List<String> detalles = new ArrayList<>();
}
