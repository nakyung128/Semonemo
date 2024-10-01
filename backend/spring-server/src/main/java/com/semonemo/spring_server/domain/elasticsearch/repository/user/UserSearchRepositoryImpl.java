package com.semonemo.spring_server.domain.elasticsearch.repository.user;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.client.elc.NativeQuery;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;

import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.BoolQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.MatchQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.NestedQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.Operator;
import co.elastic.clients.elasticsearch._types.query_dsl.TermQuery;
import co.elastic.clients.elasticsearch._types.query_dsl.WildcardQuery;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSearchRepositoryImpl implements UserSearchRepositoryCustom {

	private final ElasticsearchOperations elasticsearchOperations;
	// @Override
	// public Page<UserDocument> findByNicknameContaining(String keyword, Pageable pageable) {
	// 	BoolQuery boolQuery = BoolQuery.of(b -> b
	// 		.should(MatchQuery.of(m -> m
	// 			.field("ngram")
	// 			.query(keyword)
	// 			.analyzer("ngram_analyzer")
	// 		)._toQuery())
	// 		.should(TermQuery.of(t -> t
	// 			.field("nickname")
	// 			.value(keyword)
	// 		)._toQuery())
	// 		.minimumShouldMatch("1")
	// 	);
	//
	// 	NativeQuery nativeQuery = NativeQuery.builder()
	// 		.withQuery(boolQuery._toQuery())
	// 		.withPageable(pageable)
	// 		.build();
	//
	// 	SearchHits<UserDocument> searchHits = elasticsearchOperations.search(nativeQuery, UserDocument.class);
	//
	// 	List<UserDocument> content = searchHits.getSearchHits().stream()
	// 		.map(SearchHit::getContent)
	// 		.collect(Collectors.toList());
	//
	// 	return new PageImpl<>(content, pageable, searchHits.getTotalHits());
	// }



		// 	BoolQuery boolQuery = new BoolQuery.Builder()
	// 		.must(new MatchQuery.Builder()
	// 			.field("nickname.ngram")
	// 			.query(keyword)
	// 			.operator(Operator.And)
	// 			.build()._toQuery())
	// 		.should(new MatchQuery.Builder()
	// 			.field("nickname.keyword")
	// 			.query(keyword)
	// 			.operator(Operator.And)
	// 			.build()._toQuery())
	// 		.build();
	//
	// 	NativeQuery nativeQuery = NativeQuery.builder()
	// 		.withQuery(boolQuery._toQuery())
	// 		.withPageable(pageable)
	// 		.build();
	//
	// 	SearchHits<UserDocument> searchHits = elasticsearchOperations.search(nativeQuery, UserDocument.class);
	//
	// 	List<UserDocument> content = searchHits.getSearchHits().stream()
	// 		.map(SearchHit::getContent)
	// 		.collect(Collectors.toList());
	//
	// 	return new PageImpl<>(content, pageable, searchHits.getTotalHits());
	// }
}
