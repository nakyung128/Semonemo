package com.semonemo.spring_server.domain.user.dto.request;

public record UserLoginRequestDTO(
	String address,
	String password) {
}
