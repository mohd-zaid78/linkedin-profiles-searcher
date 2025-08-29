package com.zaid.alumnisearch.service.helper;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.zaid.alumnisearch.constants.Constants;
import com.zaid.alumnisearch.http.HttpRequest;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class AgentDataJsonHelper {
	
	@Value("${phantombuster.apiKey}")
	private String apiKey;
	
	
	public HttpRequest prepareHttpRequestForAgentDataJson(String jsonUrl) {

		
		HttpHeaders httpHeaders = new HttpHeaders();


		// populating HttpRequest fields
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setMethod(HttpMethod.GET);
		httpRequest.setHeader(httpHeaders);
		httpRequest.setUrl(jsonUrl);
		httpRequest.setBody(Constants.EMPTY_STRING);
		log.info("httpRequest Object for fetch all agent jsondata Request is created : {}", httpRequest);
		return httpRequest;
	}
	
}
