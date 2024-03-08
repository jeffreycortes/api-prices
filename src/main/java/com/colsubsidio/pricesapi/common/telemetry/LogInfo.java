/*
 * Codigo fuente propiedad de Colsubsidio
 * Gerencia de Tecnologia
 */
package com.colsubsidio.pricesapi.common.telemetry;

import lombok.Data;

/**
 *
 * @author Robert Barraza
 */
@Data
public class LogInfo {
    private String app;
    private String version;
    private String index;
    private String type;
    private String eventDate;
    private int typeStatusCode;
    private StringBuilder message;
    private String detail;
    private String spanIdString;
    private String localRootIdString;
    private String parentIdString;
    private String traceIdString;
}
