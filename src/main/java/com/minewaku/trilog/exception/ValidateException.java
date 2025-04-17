package com.minewaku.trilog.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ValidateException extends RuntimeException {
	
	private String errorCode;
	private int statusCode = HttpStatus.BAD_REQUEST.value();
	private String[] fields = new String[] {};
	private HttpStatus httpStatus = HttpStatus.BAD_REQUEST;

	public ValidateException(String message) {
		super(message);
	}
	
	public ValidateException(String message, String errorCode, HttpStatus httpStatus) {
		super(message);
		this.errorCode = errorCode;
		this.statusCode = httpStatus.value();
		this.httpStatus = httpStatus;
	}
	
	public ValidateException(String message, String errorCode, int statusCode, HttpStatus httpStatus) {
		super(message);
		this.errorCode = errorCode;
		this.statusCode = statusCode;
		this.httpStatus = httpStatus;
	}


	public ValidateException(String message, Throwable cause) {
		super(message, cause);
	}
}
