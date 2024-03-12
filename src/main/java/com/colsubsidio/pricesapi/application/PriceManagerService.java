package com.colsubsidio.pricesapi.application;

import com.colsubsidio.pricesapi.domain.PriceEntity;
import com.colsubsidio.pricesapi.domain.PriceRequestDto;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
import com.colsubsidio.pricesapi.domain.PricesRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class PriceManagerService {
    private final PricesRepository pricesRepository;

    PriceManagerService(PricesRepository pricesRepository) {
        this.pricesRepository = pricesRepository;
    }

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

    public PriceResponseDto findPrice(PriceRequestDto priceRequest) {
        var price = this.pricesRepository.findById(1L).get();

        return PriceResponseDto.builder()
                .cadenaId(price.getBrandId())
                .productoId(price.getProductId())
                .tarifa(price.getPriceList())
                .fechasAplicacion(price.getStartDate())
                .precioFinal(price.getPrice())
                .moneda(price.getCurr())
                .build();
    }

    public PriceResponseDto findPriceFinal(PriceRequestDto priceRequest) {
            var precios = this.pricesRepository
                            .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                    priceRequest.getCadenaId(),
                                    priceRequest.getProductoId(),
                                    priceRequest.getFechaAplicacion(),
                                    priceRequest.getFechaAplicacion());
        if (precios.isEmpty())
                return null;

        var price = precios.get(0);

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
