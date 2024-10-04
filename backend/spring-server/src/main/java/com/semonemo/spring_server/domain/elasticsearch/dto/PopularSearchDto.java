package com.semonemo.spring_server.domain.elasticsearch.dto;

public record PopularSearchDto(
	String keyword,
	Long count
) {
}
