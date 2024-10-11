package com.semonemo.spring_server.domain.elasticsearch.repository.searchquery;

import java.io.IOException;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;

import com.semonemo.spring_server.domain.elasticsearch.dto.PopularSearchDto;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class SearchQueryRepositoryImpl implements SearchQueryRepositoryCustom {
	private final ElasticsearchClient elasticsearchClient;

	@Override
	public List<PopularSearchDto> getPopularSearches(int days, int size) {
		try {

			SearchResponse<Void> response = elasticsearchClient.search(s -> s
					.index("search_queries-*")
					.query(q -> q
						.range(r -> r
							.field("@timestamp")
							.gte(JsonData.of(LocalDate.now().minusDays(days).atStartOfDay(ZoneOffset.UTC).toString()))
							.lte(JsonData.of(LocalDate.now().plusDays(1).atStartOfDay(ZoneOffset.UTC).toString()))

						)
					)
					.aggregations("popular_searches", a -> a
						.terms(t -> t
							.field("search_query.keyword")
							.size(size)
						)
					),
				Void.class
			);

			List<PopularSearchDto> popularSearches = new ArrayList<>();
			List<StringTermsBucket> buckets = response.aggregations()
				.get("popular_searches")
				.sterms()
				.buckets().array();

			for (StringTermsBucket bucket : buckets) {
				popularSearches.add(new PopularSearchDto(bucket.key().stringValue(), bucket.docCount()));
			}

			return popularSearches;
		} catch (IOException e) {
			throw new RuntimeException("Error querying Elasticsearch", e);
		}
	}
}