package com.colsubsidio.pricesapi.infrastructure;

import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import org.springframework.web.bind.annotation.GetMapping;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("prices/")
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
}
