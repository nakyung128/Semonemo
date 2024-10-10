package com.semonemo.spring_server.domain.elasticsearch.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;

import lombok.Data;

public record AssetSearchResponseDto(
	Long assetSellId,
	Long assetId,
	UserInfoResponseDTO creator,
	String imageUrl,
	Long price,
	Long hits,
	LocalDateTime createdAt,
	Long likeCount,
	List<String> tags,
	boolean isLiked,
	Long purchaseCount

) {
}
