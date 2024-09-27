package com.semonemo.spring_server.domain.asset.dto;

import java.util.List;

import com.semonemo.spring_server.domain.asset.model.AssetImage;

public record AssetSellRequestDto(
	Long assetId,
	Long price,
	List<String> tags
) {
}
