package com.semonemo.spring_server.domain.elasticsearch.service;

import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

public interface SearchService {

	CursorResult<AssetSearchResponseDto> searchAsset(Long nowid, String keyword, Long page, int size);

	AssetSellDocument getAsset(Long id);
}
