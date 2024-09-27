package com.semonemo.spring_server.domain.elasticsearch.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.stereotype.Component;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;

@Component
public class ElasticsearchIndexChecker {

    @Autowired
    private ElasticsearchOperations elasticsearchOperations;

    public boolean isIndexed(Long assetId) {
        return elasticsearchOperations.exists(assetId.toString(), IndexCoordinates.of("asset_sells"));
    }

    public void indexDocument(AssetSellDocument document) {
        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(document.getAssetId().toString())
                .withObject(document)
                .build();
        elasticsearchOperations.index(indexQuery, IndexCoordinates.of("asset_sells"));
    }
}