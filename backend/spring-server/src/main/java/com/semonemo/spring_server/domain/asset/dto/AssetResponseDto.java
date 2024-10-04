package com.semonemo.spring_server.domain.asset.dto;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;

public record AssetResponseDto(
	Long assetId,
	UserInfoResponseDTO creator,
	String imageUrl,
	boolean isLiked
) {
}
