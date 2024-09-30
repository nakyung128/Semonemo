package com.semonemo.spring_server.domain.asset.dto;

import java.time.LocalDateTime;
import java.util.List;

import lombok.Data;

public record AssetDetailResponseDto(
	Long assetId,
	Long assetSellId,
	Long creator,
	String imageUrl,
	LocalDateTime createAt,
	Long hits,
	Long likeCount,
	String nickname,
	Long price,
	boolean isLiked,
	List<String> tags

) {
	@Data
	public static class Tag {
		Long atagId;
		String name;
	}
}
