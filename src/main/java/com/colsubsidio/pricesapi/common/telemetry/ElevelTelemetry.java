/*
 * Codigo fuente propiedad de Colsubsidio
 * Gerencia de Tecnologia
 */
package com.colsubsidio.pricesapi.common.telemetry;

import org.apache.logging.log4j.Level;
import org.springframework.http.HttpStatus;

/**
 *
 * @author Robert Barraza
 */
public enum ElevelTelemetry {
    TRACE(Level.TRACE, HttpStatus.OK),
    DEBUG(Level.DEBUG, HttpStatus.OK),
    INFO(Level.INFO, HttpStatus.OK),
    WARN(Level.WARN, HttpStatus.INTERNAL_SERVER_ERROR),
    ERROR(Level.ERROR, HttpStatus.INTERNAL_SERVER_ERROR);

    private final HttpStatus httpStatus;
    private final Level level;

    private ElevelTelemetry(Level level, HttpStatus httpStatus) {
        this.httpStatus = httpStatus;
        this.level = level;
    }

    public HttpStatus getHttpStatus() {
        return this.httpStatus;
    }

    public Level getLevel() {
        return this.level;
    }
}
