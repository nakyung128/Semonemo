package com.semonemo.spring_server.domain.elasticsearch.dto;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;

public record UserSearchResponseDto(
	UserInfoResponseDTO userInfoResponseDTO
) {
}
