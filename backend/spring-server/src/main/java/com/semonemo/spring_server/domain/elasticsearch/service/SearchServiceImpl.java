package com.semonemo.spring_server.domain.elasticsearch.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.repository.like.AssetLikeRepository;
import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.repository.AssetElasticsearchRepository;
import com.semonemo.spring_server.global.common.CursorResult;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
	private final AssetElasticsearchRepository assetElasticsearchRepository;
	private final AssetLikeRepository assetLikeRepository;
	private final ElasticsearchOperations elasticsearchOperations;
	private  final ElasticsearchSyncService syncService;


	@PostConstruct
	public void initializeElasticsearch() {
		syncService.syncAllData();
	}
	@Override
	public CursorResult<AssetSearchResponseDto> searchAsset(Long nowid, String keyword, Long cursorId, int size) {
		CursorResult<AssetSellDocument> assetSellDocument = assetElasticsearchRepository.findByTagKeyword(keyword, cursorId, size);
		List<AssetSearchResponseDto> dtos = assetSellDocument.getContent().stream()
			.map(document -> convertToDto(nowid, document))
			.toList();
		return new CursorResult<>(dtos,assetSellDocument.getNextCursor(),assetSellDocument.isHasNext());
	}

	@Override
	public Page<AssetSearchResponseDto> findOrderBy(Long nowid, String orderBy, String keyword, int page, int size) {
		Page<AssetSellDocument> assetSellDocuments = assetElasticsearchRepository.keywordAndOrderby( keyword, orderBy, page, size);

		List<AssetSearchResponseDto> assetSellDtos = assetSellDocuments.getContent().stream()
			.map(document -> convertToDto(nowid, document))
			.toList();

		return new PageImpl<>(assetSellDtos, assetSellDocuments.getPageable(), assetSellDocuments.getTotalElements());
	}

	private AssetSearchResponseDto convertToDto (Long nowid,AssetSellDocument document){
		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid, document.getAssetSellId());

		AssetSearchResponseDto dto = new AssetSearchResponseDto(
			document.getAssetSellId(),
			document.getAssetId(),
			document.getCreator(),
			document.getImageUrls(),
			document.getPrice(),
			document.getHits(),
			document.getCreatedAt(),
			document.getLikeCount(),
			document.getTags(),
			isLiked,
			document.getPurchaseCount()
		);
		return dto;
	}

}
