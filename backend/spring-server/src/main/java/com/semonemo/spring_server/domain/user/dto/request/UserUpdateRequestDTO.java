package com.semonemo.spring_server.domain.user.dto.request;

public record UserUpdateRequestDTO(
	String nickname,
	String profileImage) {
}
