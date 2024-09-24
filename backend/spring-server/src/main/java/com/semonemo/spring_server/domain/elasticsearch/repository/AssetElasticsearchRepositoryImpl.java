package com.semonemo.spring_server.domain.elasticsearch.repository;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.global.common.CursorResult;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.*;
import co.elastic.clients.json.JsonData;

public class AssetElasticsearchRepositoryImpl implements AssetElasticsearchRepositoryCustom {

	private final ElasticsearchOperations elasticsearchOperations;

	public AssetElasticsearchRepositoryImpl(ElasticsearchOperations elasticsearchOperations) {
		this.elasticsearchOperations = elasticsearchOperations;
	}

	@Override
	public CursorResult<AssetSellDocument> findByTagKeyword(String keyword, Long cursorId, int size) {
		BoolQuery.Builder boolQueryBuilder = new BoolQuery.Builder()
				.must(new NestedQuery.Builder()
						.path("tags")
						.query(new MatchQuery.Builder()
								.field("tags.name.ngram")
								.query(keyword)
								.operator(Operator.And)
								.build()._toQuery())
						.build()._toQuery());

		if (cursorId != null) {
			boolQueryBuilder.filter(new RangeQuery.Builder()
					.field("assetSellId")
					.lt(JsonData.of(cursorId))
					.build()._toQuery());
		}

		NativeQuery query = NativeQuery.builder()
				.withQuery(boolQueryBuilder.build()._toQuery())
				.withSort(sort -> sort.field(f -> f.field("assetSellId").order(SortOrder.Desc)))
				.withPageable(PageRequest.of(0, size + 1))
				.build();

		SearchHits<AssetSellDocument> searchHits = elasticsearchOperations.search(query, AssetSellDocument.class);
//		System.out.println("Total hits: " + searchHits.getTotalHits());
//		System.out.println("Query: " + query.getQuery().toString());

		List<AssetSellDocument> results = searchHits.getSearchHits().stream()
				.map(SearchHit::getContent)
				.collect(Collectors.toList());
//		System.out.println("Results size: " + results.size());
// 		results.forEach(doc -> System.out.println("AssetSellId: " + doc.getAssetSellId()));
		boolean hasNext = results.size() > size;
		if (hasNext) {
			results = results.subList(0, size);
		}

		Long nextCursorId = hasNext ? results.get(results.size() - 1).getAssetSellId() : null;

		return new CursorResult<>(results, nextCursorId, hasNext);
	}
}