package com.semonemo.spring_server.global.common;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
@Schema(name = "CommonResponse", description = "공통 응답 객체")
public class CommonResponse<T> {

	@Schema(description = "Custom 응답 코드")
	private String code;

	@Schema(description = "응답 데이터")
	private T data;

	@Schema(description = "응답 메시지")
	private String message;

	public static <T> CommonResponse<T> success(T data, String message) {
		return CommonResponse.<T>builder()
			.code("S000")
			.data(data)
			.message(message)
			.build();
	}

	public static <T> CommonResponse<T> success(String message) {
		return CommonResponse.<T>builder()
			.code("S000")
			.data(null)
			.message(message)
			.build();
	}
}
