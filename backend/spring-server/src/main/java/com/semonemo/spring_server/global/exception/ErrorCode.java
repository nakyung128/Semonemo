package com.semonemo.spring_server.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Server
	INTERNAL_SERVER_ERROR("S001", "서버 내부 오류", HttpStatus.INTERNAL_SERVER_ERROR),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
