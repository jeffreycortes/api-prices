package com.colsubsidio.pricesapi.infrastructure.adapters;

import com.colsubsidio.pricesapi.common.ApigeeService;
import com.colsubsidio.pricesapi.common.GsonUtils;
import com.colsubsidio.pricesapi.common.telemetry.LogsManager;
import com.colsubsidio.pricesapi.tmp.domain.email.LogServices;
import com.google.gson.Gson;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

@Component
public class RestTemplateUtil {

	private static final String BEARER = "Bearer ";
	
	@Autowired
	private ApigeeService tokenService;
	@Autowired
	private RestTemplate restTemplate;
	
	@Autowired
	private LogsManager logsManager;
	
	@Autowired
	private GsonUtils gsonUtils;
	
	public <T> ResponseEntity<T>sendRequest(UriComponentsBuilder uri,HttpMethod method ,Object body,Class<T> classOfT,HttpHeaders headersAdd,boolean apigeeToken,String tag){
		long startTimeTry = System.currentTimeMillis();
		ResponseEntity<String> resp = null;
		ResponseEntity<T> result = null;
		HttpHeaders headers = headersAdd!= null ? headersAdd : new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(apigeeToken)headers.add(HttpHeaders.AUTHORIZATION, BEARER + tokenService.getToken());
		LogServices logServices = LogServices.builder()
				.url(uri.toUriString())
				.body(body)
				.method(method)
				.headers(headers)
				.error("")
				.tag(tag)
				.build();
		try {
			HttpEntity<Object> entity = null;
			entity = (body != null)?new HttpEntity<>(body,headers):new HttpEntity<>(headers);
			resp = restTemplate.exchange(uri.toUriString(), method, entity, String.class);
			
			Gson gson = new Gson();
			T respClassOfT = gson.fromJson(resp.getBody() , classOfT);
			result = new ResponseEntity<>(respClassOfT,resp.getHeaders(),resp.getStatusCode());
			long endTimeConn = System.currentTimeMillis() - startTimeTry;
			logServices.setBodyResponse(resp.getBody());
			logServices.setStatus(resp.getStatusCode());
			logServices.setTimeConn(endTimeConn);
			logsManager.info(uri.toUriString(), body, resp.getBody(), method, resp.getStatusCode(), endTimeConn,headers);	
		}catch (Exception e) {
			long endTimeConn = System.currentTimeMillis() - startTimeTry;
			logServices.setBodyResponse(resp != null?resp.getBody():e.getMessage());
			logServices.setStatus(resp != null?resp.getStatusCode():HttpStatus.INTERNAL_SERVER_ERROR);
			logServices.setTimeConn(endTimeConn);
			logServices.setError(e.getMessage());
			logsManager.error(uri.toUriString(), body, resp.getBody(), method, resp.getStatusCode(), endTimeConn,headers);
		}
		return result;
	}
	
	public <T> ResponseEntity<T>sendRequest(UriComponentsBuilder uri,HttpMethod method ,Object body,Class<T> classOfT,HttpHeaders headersAdd,boolean apigeeToken,boolean isBodyJson){
		long startTimeTry = System.currentTimeMillis();
		ResponseEntity<String> resp = null;
		ResponseEntity<T> result = null;
		HttpHeaders headers = headersAdd!= null ? headersAdd : new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if(apigeeToken)headers.add(HttpHeaders.AUTHORIZATION, BEARER + tokenService.getToken());
		LogServices logServices = LogServices.builder()
				.url(uri.toUriString())
				.body(body)
				.method(method)
				.headers(headers)
				.error("")
				.build();
		try {
			HttpEntity<Object> entity = null;
			entity = (body != null)?new HttpEntity<>(body,headers):new HttpEntity<>(headers);
			resp = restTemplate.exchange(uri.toUriString(), method, entity, String.class);
			if (classOfT != String.class) {
				T respClassOfT = new Gson().fromJson(resp.getBody(), classOfT);
				result = new ResponseEntity<>(respClassOfT,resp.getHeaders(),resp.getStatusCode());
			}else {
				@SuppressWarnings("unchecked")
				T respClassOfT = (T) resp;
				result = new ResponseEntity<>(respClassOfT,resp.getHeaders(),resp.getStatusCode());
			}
			
			long endTimeConn = System.currentTimeMillis() - startTimeTry;
			logServices.setBodyResponse(result.getBody());
			logServices.setStatus(result.getStatusCode());
			logServices.setTimeConn(endTimeConn);
			logsManager.info(uri.toUriString(), body, resp.getBody(), method, resp.getStatusCode(), endTimeConn,headers);
		}catch (Exception e) {
			long endTimeConn = System.currentTimeMillis() - startTimeTry;
			isBodyJson = isBodyJson? resp != null:isBodyJson;
			logServices.setBodyResponse(resp != null?resp.getBody():e.getMessage());
			logServices.setStatus(resp != null?resp.getStatusCode():HttpStatus.INTERNAL_SERVER_ERROR);
			logServices.setTimeConn(endTimeConn);
			logServices.setError(e.getMessage());
			logsManager.error(uri.toUriString(), body, resp.getBody(), method, resp.getStatusCode(), endTimeConn,headers);
		}
		return result;
	}
	
	public <T> ResponseEntity<T> sendRequest(UriComponentsBuilder uri, HttpMethod method, Object body,
			Class<T> classOfT, HttpHeaders headers, boolean apigeeToken) {
		long startTimeTry = System.currentTimeMillis();
		headers.setContentType(MediaType.APPLICATION_JSON);
		if (apigeeToken) {
			headers.add(HttpHeaders.AUTHORIZATION, BEARER + tokenService.getToken());
		}
		HttpEntity<Object> entity = null;
		entity = (body != null) ? new HttpEntity<>(body, headers) : new HttpEntity<>(headers);
		logsManager.info("RestTemplateUtil.sendRequest url: {}, entity: {}, method: {}", uri.toUriString(), gsonUtils.serialize(entity), gsonUtils.serialize(method));
		ResponseEntity<T> resp = restTemplate.exchange(uri.toUriString(), method, entity, classOfT);
		long endTimeConn = System.currentTimeMillis() - startTimeTry;
		logsManager.info("RestTemplateUtil.sendRequest resp: {}, endTimeConn: {}", gsonUtils.serialize(resp), endTimeConn);
		return resp;
	}
}
