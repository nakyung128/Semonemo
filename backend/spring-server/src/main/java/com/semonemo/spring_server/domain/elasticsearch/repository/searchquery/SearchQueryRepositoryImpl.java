package com.semonemo.spring_server.domain.elasticsearch.repository.searchquery;

import java.time.ZonedDateTime;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import co.elastic.clients.elasticsearch._types.aggregations.Aggregate;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsAggregate;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchAllQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.RangeQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchQueryRepositoryImpl implements SearchQueryRepositoryCustom {

	private final ElasticsearchOperations elasticsearchOperations;


	@Override
	public List<String> findTopSearchQueries(int size) {
		// NativeQuery query = NativeQuery.builder()
		// 	.withQuery(new MatchAllQuery.Builder().build()._toQuery())
		// 	.withAggregation("popular_searches",
		// 		a -> a.terms(t -> t.field("search_query").size(size)))
		// 	.build();
		//
		// SearchHits<Object> searchHits = elasticsearchOperations.search(query, Object.class);

		return null;
	}

	@Override
	public List<String> findTopSearchQueriesByTimeRange(int size, int days) {
		// ZonedDateTime now = ZonedDateTime.now();
		// ZonedDateTime fromDate = now.minusDays(days);
		//
		// NativeQuery query = NativeQuery.builder()
		// 	.withQuery(new RangeQuery.Builder()
		// 		.field("timestamp")
		// 		.gte(JsonData.of(fromDate.toString()))
		// 		.lte(JsonData.of(now.toString()))
		// 		.build()._toQuery())
		// 	.withAggregation("popular_searches",
		// 		a -> a.terms(t -> t.field("search_query").size(size)))
		// 	.build();
		//
		// SearchHits<Object> searchHits = elasticsearchOperations.search(query, Object.class);

		return null;
	}

	private List<String> extractTopTerms(SearchHits<Object> searchHits, String aggregationName) {
		return null;
	}
	}