package com.colsubsidio.pricesapi.domain;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
public class ExceptionResponse {
    String message;

    public ExceptionResponse(String message) {
        this.message = message;
    }
}
