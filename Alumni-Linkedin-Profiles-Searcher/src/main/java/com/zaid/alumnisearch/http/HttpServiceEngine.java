package com.zaid.alumnisearch.http;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestClient;
import lombok.extern.slf4j.Slf4j;


	@Slf4j
	@Component
	public class HttpServiceEngine {

		private RestClient restClient;

		public HttpServiceEngine(RestClient.Builder restClientBuilder) {
			restClient = restClientBuilder.build();
		}

		public ResponseEntity<String> makeHttpCall(HttpRequest httpRequest) {
			
			log.info("calling the external api using RestClient");
				

			ResponseEntity<String> responseEntity = restClient.method(httpRequest.getMethod())
					.uri(httpRequest.getUrl())
					.headers(header -> header.addAll(httpRequest.getHeader()))
					.body(httpRequest.getBody())
					.retrieve()
					.toEntity(String.class);
			    log.info("response comes from PhantomBuster's external Api call: {}", responseEntity);
			    return responseEntity;
			
	}
			
		

}
