package com.colsubsidio.pricesapi.infrastructure;

import com.colsubsidio.pricesapi.application.PriceManagerService;
import com.colsubsidio.pricesapi.common.DateUtils;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.domain.ErrorApi;
import com.colsubsidio.pricesapi.domain.Mensajes;
import com.colsubsidio.pricesapi.domain.PriceRequestDto;
import com.colsubsidio.pricesapi.domain.Resultado;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
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
    public Resultado getPriceFinal(
            @PathVariable("cadenaId") long cadenaId,
            @PathVariable("productoId") long productoId,
            @RequestParam String dateApply) {
        Date fechaAplicacion;
        try {
            fechaAplicacion = DateUtils.formatDate(dateApply, "yyyy-MM-dd HH:mm:ss");
        }
        catch (Exception ex) {
            var detalles = new ArrayList<String>();
            detalles.add(ex.getMessage());

            var error = ErrorApi
                    .builder()
                    .codigo(Mensajes.EX001.getCodigo())
                    .descripcion(Mensajes.EX001.getDescripcion())
                    .detalles(detalles)
                    .build();

            var resultado = Resultado.instance(HttpStatus.INTERNAL_SERVER_ERROR.value(), false, null);
            resultado.setErrorApi(error);

            return resultado;
        }

        var filter =  PriceRequestDto.builder()
                .cadenaId(cadenaId)
                .productoId(productoId)
                .fechaAplicacion(fechaAplicacion)
                .build();

        return this.priceManagerService.getPriceFinal(filter);
    }
}
