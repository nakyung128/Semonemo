package com.semonemo.spring_server.domain.elasticsearch.repository.asset;

import org.springframework.data.domain.Page;

import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

public interface AssetElasticsearchRepositoryCustom {

	CursorResult<AssetSellDocument> findByTagKeyword(String keyword, Long cursorId, int size);

	Page<AssetSellDocument> keywordAndOrderby(String keyword, String orderBy, int page, int size);

	 void updateData(Long assetSellId,String type, AssetSell assetSell);

	}
