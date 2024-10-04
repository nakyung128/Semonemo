package com.semonemo.spring_server.domain.elasticsearch.document;

import org.springframework.data.annotation.Id;
import org.springframework.data.elasticsearch.annotations.Document;
import org.springframework.data.elasticsearch.annotations.Field;
import org.springframework.data.elasticsearch.annotations.FieldType;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.ZonedDateTime;

@Document(indexName = "search_queries")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SearchQueryDocument {

	@Id
	private String id;

	@Field(type = FieldType.Keyword)
	private String search_query;

	@Field(type = FieldType.Date)
	private ZonedDateTime timestamp;

	@Field(type = FieldType.Keyword)
	private String user_id;  // 필요한 경우

	// 필요에 따라 추가 필드를 정의할 수 있습니다.
}