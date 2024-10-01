package com.semonemo.spring_server.domain.elasticsearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.RequestParam;

import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "Elasticsearch 검색", description = "Elasticsearch 기반 에셋 검색 API")
public interface ElasticSearchApi {



	@Operation(summary = "정렬된 에셋 검색 API", description = "에셋을 키워드와 정렬 조건에 따라 검색하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "에셋 키워드 검색 성공",
			content = @Content(schema = @Schema(implementation = Page.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class)))
	})
	CommonResponse<?> searchAssetOrderBy(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) String keyword,
		@RequestParam String orderBy,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size
	);
}

