package com.semonemo.spring_server.domain.asset.service;

import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.global.common.CursorResult;

public interface AssetService {
	void saveImage(AssetRequestDto assetRequestDto);

	CursorResult<AssetSellResponseDto> getAllAsset(Long nowid, Long cursorId, int size);

	AssetResponseDto getAssetDetail(Long assetId);

	AssetSellResponseDto getAssetSellDetail(Long assetSellId);

	CursorResult<AssetResponseDto> getMyAsset(Long nowid, Long cursorId, int size);

	CursorResult<AssetResponseDto> getUserAsset(Long nowid, Long userId, Long cursorId, int size);
}
