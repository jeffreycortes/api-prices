package com.colsubsidio.pricesapi.common;

import com.colsubsidio.pricesapi.tmp.domain.apigee.ApigeeTokenReqDTO;
import com.colsubsidio.pricesapi.tmp.domain.apigee.ApigeeTokenResDTO;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.http.client.HttpComponentsClientHttpRequestFactory;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.time.LocalDateTime;
import java.time.ZoneId;

@Component
public class ApigeeService {

    @Autowired
    EnvironmentService environmentService;
    private String token;
    private LocalDateTime tokenDate;

    public String getToken() {
        if (token == null) {
            generateToken();
        } else {
            long tokenMillis = tokenDate.atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            long currMillis = LocalDateTime.now().atZone(ZoneId.systemDefault()).toInstant().toEpochMilli();
            Long timeLive = currMillis - tokenMillis;
            if (timeLive > environmentService.getMaxTimeInMillis()) {
                generateToken();
            }
        }
        return token;
    }

    private void generateToken() {
        var request = ApigeeTokenReqDTO.builder()
                .clienteId(environmentService.getClienteId())
                .clienteSecreto(environmentService.getClienteSecreto())
                .build();
        token = postAccessToken(request).getAccess_token();
        tokenDate = LocalDateTime.now();
    }

    public ApigeeTokenResDTO postAccessToken(ApigeeTokenReqDTO request) {
        var restTemplate = new RestTemplate();
        restTemplate.setRequestFactory(new HttpComponentsClientHttpRequestFactory());

        var uri = UriComponentsBuilder.fromHttpUrl(environmentService.getUrlApigee()+environmentService.getTokenUrl());

        var headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);

        var entity = new HttpEntity<ApigeeTokenReqDTO>(request, headers);

        ResponseEntity<ApigeeTokenResDTO> result =
                restTemplate.exchange(uri.toUriString(), HttpMethod.POST, entity, ApigeeTokenResDTO.class);

        return result.getBody();
    }
}
