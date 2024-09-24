package com.semonemo.spring_server.domain.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetLike;
import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.asset.repository.assetimage.AssetImageRepository;
import com.semonemo.spring_server.domain.asset.repository.assetsell.AssetSellRepository;
import com.semonemo.spring_server.domain.asset.repository.like.AssetLikeRepository;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchIndexChecker;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CursorResult;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AssetServiceImpl implements AssetService {

	private final AssetImageRepository assetImageRepository;
	private final AssetSellRepository assetSellRepository;
	private final AssetLikeRepository assetLikeRepository;
	private final UserRepository userRepository;
	private final ElasticsearchSyncService syncService;
	private final ElasticsearchIndexChecker indexChecker;

	// @PostConstruct
	// public void initializeElasticsearch() {
	// 	syncService.syncAllData();
	// }
	@Transactional
	@Override
	public void saveImage(AssetRequestDto assetRequestDto) {
		AssetImage assetImage = AssetImage.builder()
			.creator(assetRequestDto.getCreator())
			.imageUrl(assetRequestDto.getImageUrl())
			.build();
		assetImageRepository.save(assetImage);
		// syncService.syncSingleAsset(assetImage.getAssetId());
	}

	@Transactional
	@Override
	public CursorResult<AssetSellResponseDto> getAllAsset(Long nowId, Long cursorId, int size) {
		List<AssetSell> assetSells;
		if (cursorId == null) {
			assetSells = assetSellRepository.findTopN(nowId, size + 1);
		} else {
			assetSells = assetSellRepository.findNextN(nowId, cursorId, size + 1);
		}
		List<AssetSellResponseDto> dtos = new ArrayList<>();
		boolean hasNext = false;
		if (assetSells.size() > size) {
			hasNext = true;
			assetSells = assetSells.subList(0, size);
		}

		for (AssetSell assetSell : assetSells) {
			AssetSellResponseDto dto = convertToDto(nowId,assetSell.getAssetSellId());
			dtos.add(dto);
		}
		Long nextCursorId = hasNext ? assetSells.get(assetSells.size() - 1).getAssetSellId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Override
	public CursorResult<AssetResponseDto> getMyAsset(Long nowId, Long cursorId, int size) {
		List<AssetImage> assetImages;
		if (cursorId == null) {
			assetImages = assetImageRepository.findByUserIdTopN(nowId, size + 1);
		} else {
			assetImages = assetImageRepository.findByUserIdNextN(nowId, cursorId, size + 1);
		}
		List<AssetResponseDto> dtos = new ArrayList<>();
		boolean hasNext = false;
		if (assetImages.size() > size) {
			hasNext = true;
			assetImages = assetImages.subList(0, size);
		}

		for (AssetImage assetImage : assetImages) {
			AssetResponseDto dto = convertToAssetDto(nowId,assetImage.getAssetId());
			dtos.add(dto);
		}
		Long nextCursorId = hasNext ? assetImages.get(assetImages.size() - 1).getAssetId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Override
	public CursorResult<AssetResponseDto> getUserAsset(Long nowid, Long userId, Long cursorId, int size) {
		List<AssetImage> assetImages;
		if (cursorId == null) {
			assetImages = assetImageRepository.findByCreatorTopN(nowid, userId, size + 1);
		} else {
			assetImages = assetImageRepository.findByCreatorIdNextN(nowid, userId, cursorId, size + 1);
		}
		List<AssetResponseDto> dtos = new ArrayList<>();
		boolean hasNext = false;
		if (assetImages.size() > size) {
			hasNext = true;
			assetImages = assetImages.subList(0, size);
		}
		for (AssetImage assetImage : assetImages) {
			AssetResponseDto dto = convertToAssetDto(nowid,assetImage.getAssetId());
			dtos.add(dto);
		}
		Long nextCursorId = hasNext ? assetImages.get(assetImages.size() - 1).getAssetId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Transactional
	@Override
	public void like(Long nowid, Long assetSellId) {
		AssetLike like = AssetLike.builder()
			.assetSellId(assetSellId)
			.userId(nowid)
			.build();
		assetLikeRepository.save(like);
		assetSellRepository.updateCount(1, assetSellId);

	}

	@Transactional
	@Override
	public void dislike(Long nowid, Long assetSellId) {
		AssetLike like = assetLikeRepository.findByUserIdAndAssetSellId(nowid, assetSellId);
		assetLikeRepository.delete(like);
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new RuntimeException("Asset Sell Not Found"));
		if (assetSell.getLikeCount() > 0) {
			assetSellRepository.updateCount(-1, assetSellId);
		}
	}

	@Transactional
	@Override
	public boolean checkLike(Long nowid, Long assetSellId) {
		return assetLikeRepository.existsByUserIdAndAssetSellId(nowid, assetSellId);
	}

	@Override
	public AssetResponseDto getAssetDetail(Long nowid,Long assetId) {
		return convertToAssetDto(nowid,assetId);
	}

	@Override
	public AssetSellResponseDto getAssetSellDetail(Long nowid, Long assetSellId) {
		return convertToDto(nowid,assetSellId);
	}

	private AssetResponseDto convertToAssetDto(Long nowid,Long assetId) {
		AssetImage assetImage = assetImageRepository.findById(assetId)
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));
		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid,assetId);
		AssetResponseDto assetResponseDto = new AssetResponseDto(
			assetImage.getAssetId(),
			assetImage.getCreator(),
			assetImage.getImageUrl(),
			isLiked
		);
		return assetResponseDto;
	}

	private AssetSellResponseDto convertToDto(Long nowid, Long assetSellId) {
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		AssetImage assetImage = assetImageRepository.findById(assetSell.getAssetId())
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid,assetSellId);
		Users user = userRepository.findById(assetImage.getCreator())
			.orElseThrow(() -> new IllegalArgumentException("User id not found"));

		AssetSellResponseDto dto = new AssetSellResponseDto(
			assetSell.getAssetId(),
			assetSell.getAssetSellId(),
			assetImage.getCreator(),
			assetImage.getImageUrl(),
			assetImage.getCreatedAt(),
			assetSell.getHits(),
			assetSell.getLikeCount(),
			user.getNickname(),
			assetSell.getPrice(),
			isLiked
		);
		return dto;
	}
}