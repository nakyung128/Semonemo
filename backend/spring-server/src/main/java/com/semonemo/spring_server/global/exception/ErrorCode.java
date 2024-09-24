package com.semonemo.spring_server.global.exception;

import org.springframework.http.HttpStatus;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum ErrorCode {
	// Server
	INTERNAL_SERVER_ERROR("S001", "서버 내부 오류", HttpStatus.INTERNAL_SERVER_ERROR),
	RESOURCE_NOT_FOUND_ERROR("S002", "요청한 리소스를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),

	// Auth
	INVALID_USER_DATA_ERROR("AU001", "유효하지 않은 값이 입력되었습니다.", HttpStatus.BAD_REQUEST),
	INVALID_NICKNAME_ERROR("AU002", "유효하지 않은 닉네임 형식입니다.", HttpStatus.BAD_REQUEST),
	AUTHENTICATION_FAIL_ERROR("AU003", "사용자 인증에 실패했습니다.", HttpStatus.UNAUTHORIZED),
	EXPIRED_TOKEN_ERROR("AU004", "만료된 토큰입니다.", HttpStatus.UNAUTHORIZED),
	INVALID_TOKEN_ERROR("AU005", "유효하지 않은 토큰입니다.", HttpStatus.UNAUTHORIZED),
	EXISTS_ADDRESS_ERROR("AU006", "이미 존재하는 주소입니다.", HttpStatus.BAD_REQUEST),

	// User
	USER_NOT_FOUND_ERROR("U001", "사용자를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),


	// Asset
	LIKE_NOT_FOUND_ERROR("AS001", "좋아요를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	LIKE_Already_exist("AS002", "이미 좋아요 눌렀습니다.", HttpStatus.ALREADY_REPORTED),
	;



	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
