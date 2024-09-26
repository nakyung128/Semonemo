package com.semonemo.spring_server.domain.elasticsearch.service;

import org.springframework.data.domain.Page;

import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

public interface SearchService {

	CursorResult<AssetSearchResponseDto> searchAsset(Long nowid, String keyword, Long page, int size);

	AssetSellDocument getAsset(Long id);

	Page<AssetSearchResponseDto> findOrderBy(Long id, String orderBy, String keyword, int page, int size);
}
