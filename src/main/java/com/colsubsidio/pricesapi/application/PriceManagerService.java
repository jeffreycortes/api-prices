package com.colsubsidio.pricesapi.application;

import com.colsubsidio.pricesapi.common.DateUtils;
import com.colsubsidio.pricesapi.domain.PriceRequestDto;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
import com.colsubsidio.pricesapi.domain.PricesRepository;
import com.colsubsidio.pricesapi.domain.Resultado;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;

@Service
public class PriceManagerService {
    private final PricesRepository pricesRepository;

    PriceManagerService(PricesRepository pricesRepository) {
        this.pricesRepository = pricesRepository;
    }

    public Resultado getPriceFinal(PriceRequestDto priceRequest) {
        var precios = this.pricesRepository
                        .findByBrandIdAndProductIdAndStartDateLessThanEqualAndEndDateGreaterThanEqualOrderByPriorityDesc(
                                priceRequest.getCadenaId(),
                                priceRequest.getProductoId(),
                                priceRequest.getFechaAplicacion(),
                                priceRequest.getFechaAplicacion());
        if (precios.isEmpty())
            return Resultado.instance(HttpStatus.NOT_FOUND, false, null);

        var price = precios.get(0);
        var priceFinal = PriceResponseDto
                            .builder()
                            .cadenaId(price.getBrandId())
                            .productoId(price.getProductId())
                            .tarifa(price.getPriceList())
                            .fechasAplicacion(DateUtils.toISO(price.getStartDate()))
                            .precioFinal(price.getPrice())
                            .moneda(price.getCurr())
                            .build();

        return Resultado.instance(HttpStatus.OK, true, priceFinal);
    }
}
