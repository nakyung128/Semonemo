package com.semonemo.spring_server.domain.asset.dto;

import java.time.LocalDateTime;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;

public record AssetSellResponseDto(
	Long assetId,
	Long assetSellId,
	UserInfoResponseDTO creator,
	String imageUrl,
	LocalDateTime createAt,
	Long hits,
	Long likeCount,
	Long price,
	boolean isLiked
) {
}
