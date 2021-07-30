package com.intuit.employee.exceptions;

import java.time.LocalDateTime;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.intuit.employee.model.ErrorResponse;

@ControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorResponse> handleSizeExceededException(final HttpServletRequest req, final Exception e) {
		return ResponseEntity.status(400).body(buildErrorResponse(e.getMessage(), 400, req.getRequestURI().toString()));
    }
	
	private ErrorResponse buildErrorResponse(String message, int statusCode, String url) {
        ErrorResponse errorResponse = new ErrorResponse();
        errorResponse.setTimestamp(LocalDateTime.now());
        errorResponse.setMessage(message);
        errorResponse.setStatus(statusCode);
        errorResponse.setPath(url);
        return errorResponse;
    }
}
