package com.semonemo.spring_server.domain.asset.service;

import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.global.common.CursorResult;

public interface AssetService {
	void saveImage(AssetRequestDto assetRequestDto);

	CursorResult<AssetSellResponseDto> getAllAsset(Long id, Long cursorId, int size);

	AssetResponseDto getAssetDetail(Long assetId);
}
