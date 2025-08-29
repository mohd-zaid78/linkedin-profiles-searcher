package com.zaid.alumnisearch.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;

@Getter
public class LinkedinsAlumniProfileExcpetion extends RuntimeException {
	
	private static final long serialVersionUID = 726788460431550882L;
	private final String status;
	private final String errorMessage;
	private final HttpStatus httpStatus;
	
	
	public LinkedinsAlumniProfileExcpetion(String status, String errorMessage , HttpStatus httpStatus) {
		super(errorMessage);
		this.status = status;
		this.errorMessage = errorMessage;
		this.httpStatus=httpStatus;
	}
	

}
