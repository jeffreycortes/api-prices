package com.colsubsidio.pricesapi.infrastructure;

import com.colsubsidio.pricesapi.application.PriceManagerService;
import com.colsubsidio.pricesapi.common.DateUtils;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.PriceRequestDto;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.Date;

@RestController
@RequestMapping("prices/")
@CrossOrigin(origins = "*")
public class PricesController  {
    private final EnvironmentService environmentService;
    private final LogsManager logsManager;
    private final PriceManagerService priceManagerService;

    PricesController(EnvironmentService environmentService, LogsManager logsManager, PriceManagerService priceManagerService) {
        this.environmentService = environmentService;
        this.logsManager = logsManager;
        this.priceManagerService = priceManagerService;
    }
    @GetMapping("/version")
    public String version() {
        logsManager.info("Version: ", environmentService.getVersion());
        return environmentService.getVersion();
    }

    @GetMapping(value = "/{cadenaId}/{productoId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public PriceResponseDto getPriceFinal(
            @PathVariable("cadenaId") long cadenaId,
            @PathVariable("productoId") long productoId,
            @RequestParam Date dateApply) {
        logsManager.info("Fecha aplicacion", dateApply);
        return PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa(1)
                .fechasAplicacion(dateApply)
                .precioFinal(BigDecimal.valueOf(35.500))
                .moneda("COP")
                .build();
    }
    @GetMapping(value = "/{cadenaId}/{productoId}/query", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public PriceResponseDto getPriceEntity(
            @PathVariable("cadenaId") long cadenaId,
            @PathVariable("productoId") long productoId,
            @RequestParam String dateApply) {
        logsManager.info("Fecha aplicacion", dateApply.toString());

        Date fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        var filter =  PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();

        return this.priceManagerService.findPriceFinal(filter);
    }


}
