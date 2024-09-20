package com.semonemo.spring_server.domain.user.dto;

import com.semonemo.spring_server.domain.user.entity.User;

import io.swagger.v3.oas.annotations.media.Schema;

public record UserRegisterDTO(
	String address,
	String password,
	String nickname,
	String profileImage) {

	public User toEntity() {
		return User.builder()
			.address(this.address)
			.password(this.password)
			.nickname(this.nickname)
			.profileImage(this.profileImage)
			.build();
	}

	@Schema(hidden = true)
	public boolean isValid() {
		return address != null && password != null && nickname != null;
	}
}
