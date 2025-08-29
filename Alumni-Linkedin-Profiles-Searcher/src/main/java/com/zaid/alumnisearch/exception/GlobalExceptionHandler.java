package com.zaid.alumnisearch.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.zaid.alumnisearch.constants.ErrorCodeEnum;
import com.zaid.alumnisearch.pojo.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {
	
	
  
    @ExceptionHandler(LinkedinsAlumniProfileExcpetion.class)
    public ResponseEntity<ErrorResponse> handleProfileNotFound(LinkedinsAlumniProfileExcpetion ex) {
    	ErrorResponse errorResponse = new ErrorResponse();
    	errorResponse.setStatus(ex.getStatus());
    	errorResponse.setMessage(ex.getErrorMessage());
    	return ResponseEntity.status(ex.getHttpStatus())
                .body(errorResponse);
    }

    @ExceptionHandler(Exception.class)
    public ResponseEntity<ErrorResponse> handleGeneric(Exception ex) {
    	ErrorResponse errorResponse = new ErrorResponse();
    	errorResponse.setStatus(ErrorCodeEnum.GENERIC_ERROR.getStatus());
    	errorResponse.setMessage(ErrorCodeEnum.GENERIC_ERROR.getErrorMessage());
    	return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                .body(errorResponse);
    }
}