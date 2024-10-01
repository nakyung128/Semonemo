package com.semonemo.spring_server.domain.elasticsearch.dto;

public record UserSearchResponseDto(
	Long userId,
	String nickname,
	String profileImageUrl
) {
}
