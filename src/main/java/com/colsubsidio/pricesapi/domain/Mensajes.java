package com.colsubsidio.pricesapi.domain;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.ResourceBundle;

@Getter
@AllArgsConstructor
public enum Mensajes {
    EX001("EX001"),
    EX002("EX002");

    private final String codigo;
    private static final ResourceBundle messages = ResourceBundle.getBundle("messages");
    public String getDescripcion() {
        return messages.getString(codigo);
    }
}
