/*
 * Codigo fuente propiedad de Colsubsidio
 * Gerencia de Tecnologia
 */
package com.colsubsidio.pricesapi.common.telemetry;

import com.colsubsidio.pricesapi.common.DateUtils;
import com.colsubsidio.pricesapi.common.EnvironmentService;
import com.colsubsidio.pricesapi.common.GsonUtils;
import com.microsoft.applicationinsights.TelemetryClient;
import com.microsoft.applicationinsights.boot.dependencies.apachecommons.lang3.StringUtils;
import com.microsoft.applicationinsights.boot.dependencies.google.common.base.Strings;
import com.microsoft.applicationinsights.core.dependencies.google.gson.Gson;
import com.microsoft.applicationinsights.core.dependencies.google.gson.GsonBuilder;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
public class LogsManager {
    private TelemetryClient telemetryClient;
    private EnvironmentService environmentService;

    LogsManager(
            TelemetryClient telemetryClient,
            EnvironmentService environmentService)
    {
        this.telemetryClient = telemetryClient;
        this.environmentService = environmentService;
    }

    private static final Logger LOGGER = LoggerFactory.getLogger( LogsManager.class );

    private static final String PARAMETER_INDICATOR_REGEX = "\\{\\}";
    private static final String PARAMETER_INDICATOR = "{}";

    private void telemetry(ElevelTelemetry level, String message, Object[] parameters ) {
        telemetry( level, message, null, parameters );
    }

    private void telemetry(ElevelTelemetry level, String message, Throwable exception, Object[] parameters ) {
        Gson gson;

        LogInfo logModel;

        StringBuilder builder;

        if( isValidLevel( level ) ) {
            gson = new GsonBuilder().setPrettyPrinting().disableHtmlEscaping().create();
            builder = constructBuilder( message, exception, parameters );

            logModel = new LogInfo();
            logModel.setApp( "this.nameApplication" );
            logModel.setVersion( "this.versionApplication");
            logModel.setEventDate( DateUtils.getDateString("yyyy-MM-dd'T'HH:mm:ss.SS") );
            logModel.setTypeStatusCode( level.getHttpStatus().value() );
            logModel.setMessage( builder );
            logModel.setType( level.toString() );

            logsBuildAppInsights( level, builder.toString(), gson.toJson( logModel ), exception );
        }
    }

    private void logsBuildAppInsights(
            ElevelTelemetry level,
            String orginalMessage,
            String detail,
            Throwable exception )
    {
        Exception exceptionTelemetry;

        if (!environmentService.isLoggingConsole())
            return;

        switch( level ) {
            case TRACE:
                LOGGER.trace( orginalMessage );
                this.telemetryClient.trackTrace( detail );
                break;
            case DEBUG:
                LOGGER.debug( orginalMessage );
                this.telemetryClient.trackTrace( detail );
                break;
            case INFO:
                LOGGER.info( orginalMessage );
                this.telemetryClient.trackTrace( detail );
                break;
            case WARN:
                LOGGER.warn( orginalMessage, exception );
                exceptionTelemetry = new Exception( detail, exception );
                this.telemetryClient.trackException( exceptionTelemetry );
                break;
            case ERROR:
                LOGGER.error( orginalMessage, exception );
                exceptionTelemetry = new Exception( detail, exception );
                this.telemetryClient.trackException( exceptionTelemetry );
                break;
        }
    }

    /**
     * Determina si el nivel del log dado esta habilitado para escribir.
     *
     * @param levelTelemetry Nivel del log a verificar.
     * @return Si el nivel del log dado esta habilitado para escribir.
     */
    private boolean isValidLevel( ElevelTelemetry levelTelemetry ) {
        ElevelTelemetry currentLevel;

        if( Strings.isNullOrEmpty( this.environmentService.getLoggingLevelApi() ) ) {
            currentLevel = ElevelTelemetry.ERROR;
        }
        else {
            currentLevel = ElevelTelemetry.valueOf( this.environmentService.getLoggingLevelApi().toUpperCase());
        }

        return currentLevel.getLevel().isLessSpecificThan( levelTelemetry.getLevel() );
    }

    @Async
    public void trace( String message, Object... parameters ) {
        telemetry( ElevelTelemetry.TRACE, message, parameters );
    }

    @Async
    public void debug( String message, Object... parameters ) {
        telemetry( ElevelTelemetry.DEBUG, message, parameters );
    }

    @Async
    public void info( String message, Object... parameters ) {
        telemetry( ElevelTelemetry.INFO, message, parameters );
    }

    @Async
    public void warn( String message, Object... parameters ) {
        telemetry( ElevelTelemetry.WARN, message, parameters );
    }

    @Async
    public void warn( String message, Throwable exception, Object... parameters ) {
        telemetry( ElevelTelemetry.WARN, message, exception, parameters );
    }

    @Async
    public void error( String message, Object... parameters ) {
        telemetry( ElevelTelemetry.ERROR, message, parameters );
    }

    @Async
    public void error( String message, Throwable exception, Object... parameters ) {
        telemetry( ElevelTelemetry.ERROR, message, exception, parameters );
    }

    /**
     * Construye un <code>StringBuilder</code> basado en los par�metros que se pasan.
     *
     * @param message Cadena normal, donde se encuentre la cadena "{}" se sustituir� por el respectivo par�metro en
     * orden.
     * @param exception Excepcion.
     * @param parameters Parametros que ser�n sustituidos por cada ocurrencia de "{}" en orden.
     * @return <code>StringBuilder</code> basado en los par�metros que se pasan.
     */
    public StringBuilder constructBuilder( String message, Throwable exception, Object... parameters ) {
        StringBuilder builder;

        String[] messageSplit;

        int i, parameterLength, paramFound;

        builder = new StringBuilder();

        messageSplit = message.split( LogsManager.PARAMETER_INDICATOR_REGEX );

        i = 0;
        parameterLength = parameters.length;
        paramFound = StringUtils.countMatches( message, LogsManager.PARAMETER_INDICATOR );

        for( String messagePart : messageSplit ) {
            builder.append( messagePart );

            if( i < parameterLength && i < paramFound ) {
                builder.append( parameters[ i++ ] );
            } else {
                builder.append(GsonUtils.getValueJsonString(parameters));
            }
        }

        if( exception != null ) {
            builder.append( " Message: " );
            builder.append( exception.getMessage() );
        }

        return builder;
    }
}
