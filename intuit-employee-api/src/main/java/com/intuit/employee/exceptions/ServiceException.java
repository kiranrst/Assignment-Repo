package com.intuit.employee.exceptions;

import org.springframework.http.HttpStatus;

import lombok.Data;

@Data
public class ServiceException extends Exception {

	
	private static final long serialVersionUID = 1L;

	private String message;

	private HttpStatus httpStatus;


	public ServiceException(HttpStatus httpStatus, String message) {
		super(message);
		this.httpStatus = httpStatus;
		this.message = message;
	}
}
