package com.colsubsidio.pricesapi.infrastructure;

import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import io.swagger.annotations.Api;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Api(tags = "Manejador de precios")
@CrossOrigin(origins = "*")
public class ApiController {
    EnvironmentService environmentService;
    private final LogsManager logsManager;
    ApiController(EnvironmentService environmentService, LogsManager logsManager) {
        this.environmentService = environmentService;
        this.logsManager = logsManager;
    }
    @GetMapping("/version")
    public String version() {
        logsManager.info("Version: ", environmentService.getVersion());
        return environmentService.getVersion();
    }
}
