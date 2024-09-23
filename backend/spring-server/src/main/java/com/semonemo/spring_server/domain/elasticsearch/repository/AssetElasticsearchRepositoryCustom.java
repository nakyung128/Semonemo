package com.semonemo.spring_server.domain.elasticsearch.repository;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

public interface AssetElasticsearchRepositoryCustom {

	CursorResult<AssetSellDocument> findByTagKeyword(String keyword, Long cursorId, int size);

}
