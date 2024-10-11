package com.semonemo.spring_server.domain.user.dto.response;

import com.semonemo.spring_server.domain.user.entity.Users;

import lombok.Builder;

@Builder
public record UserInfoResponseDTO(
	long userId,
	String address,
	String nickname,
	String profileImage) {

	public static UserInfoResponseDTO fromEntity(Users user) {
		return UserInfoResponseDTO.builder()
			.userId(user.getId())
			.address(user.getAddress())
			.nickname(user.getNickname())
			.profileImage(user.getProfileImage())
			.build();
	}
}
