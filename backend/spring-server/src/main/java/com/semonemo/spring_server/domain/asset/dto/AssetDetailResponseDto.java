package com.semonemo.spring_server.domain.asset.dto;

import java.time.LocalDateTime;
import java.util.List;

import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;

import lombok.Data;

public record AssetDetailResponseDto(
	Long assetId,
	Long assetSellId,
	UserInfoResponseDTO creator,
	String imageUrl,
	LocalDateTime createAt,
	Long hits,
	Long likeCount,
	Long price,
	boolean isLiked,
	List<String> tags
) {

}

