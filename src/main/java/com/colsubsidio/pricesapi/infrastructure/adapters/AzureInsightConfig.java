package com.colsubsidio.pricesapi.infrastructure.adapters;

import com.microsoft.applicationinsights.telemetry.RequestTelemetry;
import com.microsoft.applicationinsights.telemetry.Telemetry;
import org.springframework.context.annotation.Configuration;
import com.microsoft.applicationinsights.extensibility.TelemetryProcessor;

@Configuration
public class AzureInsightConfig implements TelemetryProcessor {

    private static final String ALWAYSON_METHOD_NAME = "GET /version";
    private static final String RES_CODIGO_404 = "404";

    public boolean process(Telemetry telemetry) {
        var requestTelemetry = (RequestTelemetry) telemetry;
        var name = requestTelemetry.getName();
        var responseCode = requestTelemetry.getResponseCode();
        return !(name.equals(ALWAYSON_METHOD_NAME) || responseCode.equals(RES_CODIGO_404));
    }
}
