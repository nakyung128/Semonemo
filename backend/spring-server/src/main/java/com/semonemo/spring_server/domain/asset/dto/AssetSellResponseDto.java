package com.semonemo.spring_server.domain.asset.dto;

import java.time.LocalDateTime;

public record AssetSellResponseDto(
	Long assetId,
	Long assetSellId,
	Long creator,
	String imageUrl,
	LocalDateTime createAt,
	Long hits,
	Long likeCount,
	String nickname,
	Long price,
	boolean isLiked
) {
}
