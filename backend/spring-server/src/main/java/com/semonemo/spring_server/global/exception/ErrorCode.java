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
	AUTHENTICATION_FAIL_ERROR("AU003", "사용자 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
	EXPIRED_TOKEN_ERROR("AU004", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN_ERROR("AU005", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
