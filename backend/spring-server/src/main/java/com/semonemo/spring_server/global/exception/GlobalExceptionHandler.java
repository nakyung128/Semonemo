package com.semonemo.spring_server.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse<?>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();

		return new ResponseEntity<>(
			ErrorResponse.error(errorCode),
			errorCode.getHttpStatus()
		);
	}
}
