package com.semonemo.spring_server.domain.elasticsearch.service;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

public interface SearchService {

	public CursorResult<AssetSellDocument> searchAsset(Long nowid,String keyword, Long page, int size);

	public AssetSellDocument getAsset(Long id);
}
