package com.semonemo.spring_server.domain.asset.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.asset.repository.assetimage.AssetImageRepository;
import com.semonemo.spring_server.domain.asset.repository.assetsell.AssetSellRepository;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchIndexChecker;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CursorResult;

import jakarta.transaction.Transactional;

@Service
public class AssetServiceImpl implements AssetService {
	private AssetImageRepository assetImageRepository;
	private AssetSellRepository assetSellRepository;
	private UserService userService;
	private final ElasticsearchSyncService syncService;
	private final ElasticsearchIndexChecker indexChecker;

	public AssetServiceImpl(AssetImageRepository assetImageRepository, ElasticsearchSyncService syncService,
		AssetSellRepository assetSellRepository, ElasticsearchIndexChecker indexChecker, UserService userService) {
		this.assetImageRepository = assetImageRepository;
		this.assetSellRepository = assetSellRepository;
		this.syncService = syncService;
		this.indexChecker = indexChecker;
		this.userService = userService;
	}

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
		System.out.println(assetImage.toString());
		assetImageRepository.save(assetImage);
		syncService.syncSingleAsset(assetImage.getAssetId());
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
			AssetSellResponseDto dto = convertToDto(assetSell.getAssetSellId());
			dtos.add(dto);
		}

		Long nextCursorId = hasNext ? assetSells.get(assetSells.size() - 1).getAssetSellId() : null;
		return new CursorResult<>(dtos, nextCursorId, hasNext);
	}

	@Override
	public AssetResponseDto getAssetDetail(Long assetId) {
		AssetImage assetImage = assetImageRepository.findById(assetId).
			orElseThrow(() -> new IllegalArgumentException("Asset id not found"));
		AssetResponseDto assetResponseDto = new AssetResponseDto(
			assetImage.getAssetId(),
			assetImage.getCreator(),
			assetImage.getImageUrl()
		);
		return assetResponseDto;
	}

	@Override
	public AssetSellResponseDto getAssetSellDetail(Long assetSellId) {
		return convertToDto(assetSellId);
	}

	private AssetSellResponseDto convertToDto(Long assetSellId) {
		AssetSell assetSell = assetSellRepository.findById(assetSellId)
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));

		AssetImage assetImage = assetImageRepository.findById(assetSell.getAssetId())
			.orElseThrow(() -> new IllegalArgumentException("Asset id not found"));
		Users user = userService.findById(assetImage.getCreator());
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
			false
		);
		return dto;
	}
}