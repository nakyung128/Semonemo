package com.semonemo.spring_server.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.semonemo.spring_server.global.CommonResponse;

@RestControllerAdvice
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<CommonResponse<?>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		return new ResponseEntity<>(
			CommonResponse.builder()
				.status(errorCode.getCode())
				.data(null)
				.message(errorCode.getMessage())
				.build(),
			errorCode.getHttpStatus()
		);
	}
}
