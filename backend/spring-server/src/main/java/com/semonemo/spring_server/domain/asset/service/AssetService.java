package com.semonemo.spring_server.domain.asset.service;

import org.springframework.data.domain.Page;
import org.springframework.security.core.userdetails.UserDetails;

import com.semonemo.spring_server.domain.asset.dto.AssetDetailResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.global.common.CursorResult;

public interface AssetService {
	void saveImage(AssetRequestDto assetRequestDto);

	CursorResult<AssetSellResponseDto> getAllAsset(Long nowid, Long cursorId, int size);

	Page<AssetSellResponseDto> getAllAssetSort(Long nowid, String orderBy, int page, int size);

	AssetResponseDto getAssetDetail(Long nowid, Long assetId);

	AssetDetailResponseDto getAssetSellDetail(Long nowid, Long assetSellId);

	CursorResult<AssetResponseDto> getMyAsset(Long nowid, Long cursorId, int size);

	CursorResult<AssetResponseDto> getUserAsset(Long nowid, Long userId, Long cursorId, int size);

	void like(Long nowid, Long assetSellId);

	void dislike(Long nowid, Long assetSellId);

	boolean checkLike(Long userId, Long assetSellId);

	void registSale(Long nowid, AssetSellRequestDto assetSellRequestDto);

}
