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
public class FetchAgentDataHelper {


	@Value("${phantomBuster.fetchDataUrl}")
	private String fetchDataUrl;
	
	@Value("${phantombuster.apiKey}")
	private String apiKey;
	
	
	public HttpRequest prepareHttpRequestForFetchAgentData(String containerId) {

		String url = fetchDataUrl.replace("{containerId}", containerId);

		 
		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(Constants.X_PHANTOMBUSTER_KEY_1, apiKey);


		// populating HttpRequest fields
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setMethod(HttpMethod.GET);
		httpRequest.setHeader(httpHeaders);
		httpRequest.setUrl(url);
		httpRequest.setBody(Constants.EMPTY_STRING);
		log.info("httpRequest Object for fetchAgentData Request is created : {}", httpRequest);
		return httpRequest;
	}
	
}
