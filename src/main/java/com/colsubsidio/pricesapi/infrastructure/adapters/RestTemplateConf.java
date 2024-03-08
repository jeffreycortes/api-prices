package com.colsubsidio.pricesapi.infrastructure.adapters;

import com.colsubsidio.pricesapi.infrastructure.exceptions.RestTemplateResponseErrorHandler;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.web.client.RestTemplate;


@Configuration
public class RestTemplateConf {
	
	@Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {

        return builder
        		.errorHandler(new RestTemplateResponseErrorHandler())
        		.requestFactory(HttpComponentsClientHttpRequestFactory.class)
                .build();
    }
}
