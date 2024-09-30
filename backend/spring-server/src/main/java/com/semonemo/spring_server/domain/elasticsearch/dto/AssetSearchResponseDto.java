package com.semonemo.spring_server.domain.elasticsearch.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;

import lombok.Data;

public record AssetSearchResponseDto(
	Long assetSellId,
	Long assetId,
	Long creator,
	String imageUrls,
	Long price,
	Long hits,
	LocalDateTime createdAt,
	Long likeCount,
	List<AssetSellDocument.Tag> tags,
	boolean isLiked,
	Long purchaseCount

) {
}
