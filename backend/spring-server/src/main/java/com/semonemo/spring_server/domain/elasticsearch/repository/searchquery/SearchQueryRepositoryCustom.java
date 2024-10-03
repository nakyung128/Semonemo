package com.semonemo.spring_server.domain.elasticsearch.repository.searchquery;

import java.util.List;

public interface SearchQueryRepositoryCustom {
	List<String> findTopSearchQueries(int size);

	List<String> findTopSearchQueriesByTimeRange(int size, int days);
}
