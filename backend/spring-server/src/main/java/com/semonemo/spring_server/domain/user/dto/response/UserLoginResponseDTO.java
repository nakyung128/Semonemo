package com.semonemo.spring_server.domain.user.dto.response;

public record UserLoginResponseDTO(
	String accessToken,
	String refreshToken) {
}
