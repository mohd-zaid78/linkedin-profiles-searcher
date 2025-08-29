package com.zaid.alumnisearch.service;

import org.springframework.http.ResponseEntity;

import com.zaid.alumnisearch.pojo.AlumniResponse;
import com.zaid.alumnisearch.pojo.SearchRequest;

public interface AlumniService {

	public ResponseEntity<AlumniResponse> searchAlumniProfile(SearchRequest request);

}
