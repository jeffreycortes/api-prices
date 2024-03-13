package com.colsubsidio.pricesapi.common;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Getter
@Service
public class EnvironmentService {
    @Value("${spring.application.version}")
    private String version;

    @Value("${logging.level.api}")
    private String loggingLevelApi;
    @Value("${logging.console}")
    private boolean loggingConsole;

    @Value("${security.api-key}")
    private String apiKey;
}

