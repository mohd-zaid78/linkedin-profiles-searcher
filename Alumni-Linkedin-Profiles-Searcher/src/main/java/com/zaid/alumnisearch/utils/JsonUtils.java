package com.zaid.alumnisearch.utils;

import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
@RequiredArgsConstructor
public class JsonUtils {
	
private final ObjectMapper objectMapper;
	
	
	public String toJson(Object obj) {
		String requestBodyAsJson=null;
		
		try {
			requestBodyAsJson=objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(obj);
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return requestBodyAsJson;
	}
	
	
	public <T> T fromJson(String json, Class<T> t) {
		T objectResponse=null;
		
		try {
			objectResponse=objectMapper.readValue(json, t);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
		
		return objectResponse;
	}
}
