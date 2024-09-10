package com.semonemo.spring_server.global;

import lombok.Builder;
import lombok.Getter;

@Getter
@Builder
public class CommonResponse<T> {
	private String status;
	private T data;
	private String message;
}
