package com.colsubsidio.pricesapi.infrastructure;

import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.PriceResponseDto;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("prices/")
@CrossOrigin(origins = "*")
public class PricesController  {
    EnvironmentService environmentService;
    private final LogsManager logsManager;
    PricesController(EnvironmentService environmentService, LogsManager logsManager) {
        this.environmentService = environmentService;
        this.logsManager = logsManager;
    }
    @GetMapping("/version")
    public String version() {
        logsManager.info("Version: ", environmentService.getVersion());
        return environmentService.getVersion();
    }

    @GetMapping(value = "/{cadenaId}/{productoId}", produces = "application/json")
    @ResponseStatus(HttpStatus.OK)
    public PriceResponseDto getPriceFinal(
            @PathVariable("cadenaId") String cadenaId,
            @PathVariable("productoId") String productoId,
            @RequestParam String dateApply) {
        logsManager.info("Fecha aplicacion", dateApply);
        return PriceResponseDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .tarifa("35.50")
                .fechasAplicacion(dateApply)
                .build();
    }
}
