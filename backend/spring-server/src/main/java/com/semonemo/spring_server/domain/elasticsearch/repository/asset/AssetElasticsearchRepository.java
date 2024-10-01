package com.semonemo.spring_server.domain.elasticsearch.repository.asset;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;

public interface AssetElasticsearchRepository extends ElasticsearchRepository<AssetSellDocument, Long>,AssetElasticsearchRepositoryCustom {

}
