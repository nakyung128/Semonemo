package com.semonemo.spring_server.global.exception;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "ErrorResponse", description = "공통 에러 객체")
public class ErrorResponse<T> {

	@Schema(description = "에러 코드")
	private String errorCode;

	@Schema(description = "에러 메시지")
	private String message;

	public static <T> ErrorResponse<T> error(ErrorCode errorCode) {
		return ErrorResponse.<T>builder()
			.errorCode(errorCode.getCode())
			.message(errorCode.getMessage())
			.build();
	}
}
