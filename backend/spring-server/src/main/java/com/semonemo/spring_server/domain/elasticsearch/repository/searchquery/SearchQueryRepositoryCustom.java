package com.semonemo.spring_server.domain.elasticsearch.repository.searchquery;

import java.util.List;

import com.semonemo.spring_server.domain.elasticsearch.dto.PopularSearchDto;

public interface SearchQueryRepositoryCustom {

	List<PopularSearchDto> getPopularSearches(int days, int size);
}
