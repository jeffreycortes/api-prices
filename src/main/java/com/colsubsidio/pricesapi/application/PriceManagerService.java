package com.colsubsidio.pricesapi.application;

import com.colsubsidio.pricesapi.domain.PriceEntity;
import com.colsubsidio.pricesapi.domain.PriceRequestDto;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceManagerService {
    public String getFinalPrice() {
        return "ok";
    }

    public PriceResponseDto getFinalPrice(PriceRequestDto priceRequest) {
        var price = new PriceEntity();

        price.setId(1);
        price.setPrice(BigDecimal.valueOf(35.500));
        price.setPriceList(1);
        price.setCurr("COP");
        price.setPriority((short) 2);

        return PriceResponseDto.builder()
                .cadenaId(price.getBrandId())
                .productoId(price.getProductId())
                .tarifa(price.getPriceList())
                .fechasAplicacion(price.getStartDate())
                .precioFinal(price.getPrice())
                .moneda(price.getCurr())
                .build();
    }
}
