package com.zaid.alumnisearch.service.helper;


import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;

import com.zaid.alumnisearch.constants.Constants;
import com.zaid.alumnisearch.http.HttpRequest;
import com.zaid.alumnisearch.launchagent.reqbody.Argument;
import com.zaid.alumnisearch.launchagent.reqbody.LaunchAgentRequestBody;
import com.zaid.alumnisearch.pojo.SearchRequest;
import com.zaid.alumnisearch.utils.JsonUtils;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class LaunchPhantomAgentHelper {

	@Value("${phantomBuster.launchAgentUrl}")
	private String lauchAgentUrl;
	
	@Value("${phantombuster.apiKey}")
	private String apiKey;
	
	@Value("${launchAgentRequestBody.id}")
	private String agentId;
	
	@Value("${launchAgentRequestBody.sessionCookie}")
	private String sessionCookie;

	
	private final JsonUtils jsonUtils;

	public HttpRequest prepareHttpRequestForAgentLaunch(SearchRequest searchRequest) {

		HttpHeaders httpHeaders = new HttpHeaders();
		httpHeaders.set(Constants.X_PHANTOMBUSTER_KEY_1, apiKey);
		httpHeaders.set(Constants.CONTENT_TYPE,Constants.APPLICATION_JSON);

		Argument argument = new Argument();
		argument.setSessionCookie(sessionCookie);
		argument.setSearch(searchRequest.getDesignation()+" "+searchRequest.getUniversity());
		argument.setNumberOfResults(Constants.NUMBER_OF_RESULTS);
		argument.setMaxPages(Constants.MAX_PAGES);

		LaunchAgentRequestBody launchAgentRequestBody = new LaunchAgentRequestBody();
		launchAgentRequestBody.setId(agentId);
		launchAgentRequestBody.setArgument(argument);

		String requestBodyAsJson = jsonUtils.toJson(launchAgentRequestBody);
		log.info(requestBodyAsJson);
		
		if (requestBodyAsJson == null) {
			log.error("during making the HttpRequest object for launchAgent call requestBody as JSon is null");
			throw new RuntimeException("requestBodyAsJson is null during making the LaunchPhantomAgent HttpRequest object");
		}

		// populating HttpRequest fields
		HttpRequest httpRequest = new HttpRequest();
		httpRequest.setMethod(HttpMethod.POST);
		httpRequest.setHeader(httpHeaders);
		httpRequest.setUrl(lauchAgentUrl);
		httpRequest.setBody(requestBodyAsJson);
		log.info("httpRequest Object for LaunchPhantomAgent Request is created : {}", httpRequest);
		return httpRequest;
	}
}
