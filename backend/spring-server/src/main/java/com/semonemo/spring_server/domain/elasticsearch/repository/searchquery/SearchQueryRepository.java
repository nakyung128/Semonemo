package com.semonemo.spring_server.domain.elasticsearch.repository.searchquery;

import java.util.List;

import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.semonemo.spring_server.domain.elasticsearch.document.SearchQueryDocument;

import org.springframework.stereotype.Repository;

@Repository
public interface SearchQueryRepository
	extends ElasticsearchRepository<SearchQueryDocument, Long>, SearchQueryRepositoryCustom {


}
