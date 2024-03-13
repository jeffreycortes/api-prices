package com.colsubsidio.pricesapi.domain;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class Resultado {
    private HttpStatus statusCode;
    private boolean success;
    private ErrorApi errorApi;
    private PriceResponseDto data;

    public static Resultado instance(
                            HttpStatus statusCode,
                            boolean success,
                            PriceResponseDto data) {
        var result = new Resultado();
        result.setStatusCode(statusCode);
        result.setSuccess(success);
        result.setData(data);
        return result;
    }
}
