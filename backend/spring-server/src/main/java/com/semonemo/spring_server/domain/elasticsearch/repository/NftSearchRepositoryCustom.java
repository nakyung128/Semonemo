package com.semonemo.spring_server.domain.elasticsearch.repository;

import org.springframework.data.domain.Page;

import com.semonemo.spring_server.domain.asset.model.AssetSell;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.document.NFTSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

public interface NftSearchRepositoryCustom {

	Page<NFTSellDocument> keywordAndOrderby(String keyword, String orderBy, int page, int size);

	 void updateData(Long assetSellId,String type, AssetSell assetSell);

	}
