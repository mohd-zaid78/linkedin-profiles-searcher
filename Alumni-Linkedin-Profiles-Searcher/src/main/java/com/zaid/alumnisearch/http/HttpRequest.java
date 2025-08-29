package com.zaid.alumnisearch.http;

import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpMethod;
import org.springframework.stereotype.Component;



import lombok.Data;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@Data
public class HttpRequest {
	
	private HttpMethod method;
	private String url;
	private HttpHeaders header;										
	private Object body;

}
