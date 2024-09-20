package com.semonemo.spring_server.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Server
	INTERNAL_SERVER_ERROR("S001", "서버 내부 오류", HttpStatus.INTERNAL_SERVER_ERROR),

	// Auth
	INVALID_USER_DATA_ERROR("AU001", "유효하지 않은 값이 입력되었습니다.", HttpStatus.BAD_REQUEST),
	INVALID_NICKNAME_ERROR("AU002", "유효하지 않은 닉네임 형식입니다.", HttpStatus.BAD_REQUEST),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
