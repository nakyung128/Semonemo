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
	SELF_FOLLOW_ERROR("U002", "자기 자신은 팔로우할 수 없습니다.", HttpStatus.BAD_REQUEST),
	ALREADY_FOLLOW_ERROR("U003", "이미 팔로우한 사용자입니다.", HttpStatus.BAD_REQUEST),
	NOT_FOLLOW_ERROR("U004", "팔로우 목록에 없는 사용자입니다.", HttpStatus.NOT_FOUND),
	CHECK_SELF_FOLLOW_ERROR("U005", "자기 자신은 확인할 수 없습니다.", HttpStatus.BAD_REQUEST),

	// NFT
	NFT_NOT_FOUND_ERROR("N001", "NFT를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
	USER_NFT_NOT_FOUND_ERROR("N002", "유저 NFT 조회를 실패했습니다", HttpStatus.NOT_FOUND),
	MINT_NFT_FAIL("N003", "NFT 발행에 실패했습니다.", HttpStatus.BAD_REQUEST),
    OWNER_NOT_MATCH("N004", "본인소유의 NFT가 아닙니다.", HttpStatus.BAD_REQUEST),

	// NFT MARKET
	NFT_MARKET_NOT_FOUND_ERROR("NM001", "NFT 판매 정보를 찾을 수 없습니다.", HttpStatus.NOT_FOUND),
    NFT_ALREADY_ON_SALE("NM002", "이미 판매중인 NFT 입니다.", HttpStatus.BAD_REQUEST),
    MARKET_CREATE_FAIL("NM003", "NFT 판매 등록에 실패했습니다.", HttpStatus.BAD_REQUEST),

	// 기타
	NODE_SERVER_ERROR("E001", "Block Chain 서버 오류", HttpStatus.INTERNAL_SERVER_ERROR),
	JSON_PARSING_ERROR("E002", "JSON PARSING 에러", HttpStatus.INTERNAL_SERVER_ERROR),
	;

	private final String code;
	private final String message;
	private final HttpStatus httpStatus;
}
