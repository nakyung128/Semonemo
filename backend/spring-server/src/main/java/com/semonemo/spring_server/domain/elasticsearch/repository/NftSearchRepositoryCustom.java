package com.semonemo.spring_server.domain.elasticsearch.repository;

import org.springframework.data.domain.Page;

import com.semonemo.spring_server.domain.elasticsearch.document.NFTSellDocument;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;

public interface NftSearchRepositoryCustom {

	Page<NFTSellDocument> keywordAndOrderby(String keyword, String orderBy, int page, int size);

	void updateData(Long nftSellId, String type, NFTMarket nftMarket);

}
