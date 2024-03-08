package com.colsubsidio.pricesapi.common;

import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Service
public class RestTemplateService {
	private static final String BEARER = "Bearer ";

	@Autowired
	ApigeeService tokenService;

	@Autowired
	RestTemplate restTemplate;
	@Autowired
    LogsManager logsManager;

	public <T> ResponseEntity<T> sendRequest(UriComponentsBuilder uri, HttpMethod method, Object body,
			Class<T> classOfT, boolean apigeeToken) {
		long startTimeTry = System.currentTimeMillis();
		var headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (apigeeToken) {
			headers.add(HttpHeaders.AUTHORIZATION, BEARER + tokenService.getToken());
		}
		HttpEntity<Object> entity = null;
		entity = (body != null) ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);
		ResponseEntity<T> resp = restTemplate.exchange(uri.toUriString().replace("%20", " "), method, entity, classOfT);
		long endTimeConn = System.currentTimeMillis() - startTimeTry;
		logsManager.info(uri.toUriString(), body, resp.getBody(), method, resp.getStatusCode(),
				endTimeConn, headers);
		return resp;
	}

	public <T> ResponseEntity<T> sendRequest(UriComponentsBuilder uri, HttpMethod method, Object body,
			Class<T> classOfT, boolean apigeeToken, HttpHeaders headers, boolean bodyIsJson) {
		long startTimeTry = System.currentTimeMillis();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (apigeeToken) {
			headers.add(HttpHeaders.AUTHORIZATION, BEARER + tokenService.getToken());
		}
		HttpEntity<Object> entity = null;
		entity = (body != null) ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);
		ResponseEntity<T> resp = restTemplate.exchange(uri.toUriString(), method, entity, classOfT);
		long endTimeConn = System.currentTimeMillis() - startTimeTry;
		logsManager.info(uri.toUriString(), body, resp.getBody(), method, resp.getStatusCode(),
				endTimeConn, headers, bodyIsJson);
		return resp;
	}

	public <T> ResponseEntity<T> sendRequestAsync(UriComponentsBuilder uri, HttpMethod method, Object body,
			Class<T> classOfT, boolean apigeeToken) {
		long startTimeTry = System.currentTimeMillis();
		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (apigeeToken) {
			headers.add(HttpHeaders.AUTHORIZATION, BEARER + tokenService.getToken());
		}
		HttpEntity<Object> entity = null;
		entity = (body != null) ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);
		ResponseEntity<T> resp = restTemplate.exchange(uri.toUriString(), method, entity, classOfT);
		long endTimeConn = System.currentTimeMillis() - startTimeTry;
		logsManager.info(uri.toUriString(), body, resp.getBody(), method,
				resp.getStatusCode(), endTimeConn);
		return resp;
	}
}
