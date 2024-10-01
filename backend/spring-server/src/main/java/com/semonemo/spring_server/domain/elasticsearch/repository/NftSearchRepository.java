package com.semonemo.spring_server.domain.elasticsearch.repository;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.semonemo.spring_server.domain.elasticsearch.document.NFTSellDocument;

public interface NftSearchRepository extends ElasticsearchRepository<NFTSellDocument,Long>,NftSearchRepositoryCustom {
}
