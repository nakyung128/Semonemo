package com.semonemo.spring_server.global.exception;

import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.NoHandlerFoundException;

import lombok.extern.slf4j.Slf4j;

@RestControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	@ExceptionHandler(CustomException.class)
	public ResponseEntity<ErrorResponse<?>> handleCustomException(CustomException e) {
		ErrorCode errorCode = e.getErrorCode();
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<ErrorResponse<?>> handleNoHandlerFoundException(NoHandlerFoundException e) {
		ErrorCode errorCode = ErrorCode.RESOURCE_NOT_FOUND_ERROR;
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse<?>> handleMethodArgumentNotValidException(MethodArgumentNotValidException e) {
		ErrorCode errorCode = ErrorCode.BAD_REQUEST_ERROR;
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}

	@ExceptionHandler(HttpRequestMethodNotSupportedException.class)
	public ResponseEntity<ErrorResponse<?>> handleHttpRequestMethodNotSupportedException(
		HttpRequestMethodNotSupportedException e) {
		ErrorCode errorCode = ErrorCode.METHOD_NOT_ALLOWED_ERROR;
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}

	@ExceptionHandler(HttpMessageNotReadableException.class)
	public ResponseEntity<ErrorResponse<?>> handleHttpMessageNotReadableException(HttpMessageNotReadableException e) {
		ErrorCode errorCode = ErrorCode.BAD_REQUEST_ERROR;
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}

	@ExceptionHandler(IllegalArgumentException.class)
	public ResponseEntity<ErrorResponse<?>> handleIllegalArgumentException(IllegalArgumentException e) {
		ErrorCode errorCode = ErrorCode.BAD_REQUEST_ERROR;
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}

	@ExceptionHandler(Exception.class)
	public ResponseEntity<ErrorResponse<?>> handleException(Exception e) {
		ErrorCode errorCode = ErrorCode.INTERNAL_SERVER_ERROR;
		return new ResponseEntity<>(ErrorResponse.error(errorCode), errorCode.getHttpStatus());
	}
}
