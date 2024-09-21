package com.semonemo.spring_server.domain.user.dto.request;

import com.semonemo.spring_server.domain.user.entity.Users;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRegisterRequestDTO(
	String address,
	String password,
	String nickname,
	String profileImage) {

	public Users toEntity() {
		return Users.builder()
			.address(this.address)
			.password(this.password)
			.nickname(this.nickname)
			.profileImage(this.profileImage)
			.build();
	}

	@Schema(hidden = true)
	public boolean isValid() {
		return address != null && password != null && nickname != null &&
			!address.isBlank() && !password.isBlank() && !nickname.isBlank();
	}
}
