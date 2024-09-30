package com.semonemo.spring_server.domain.asset.dto;

public record AssetResponseDto(
	Long assetId,
	Long creator,
	String imageUrl,
	boolean isLiked
) {
}
