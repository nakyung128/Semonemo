package com.semonemo.spring_server.domain.elasticsearch.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.annotations.Query;
import org.springframework.data.elasticsearch.repository.ElasticsearchRepository;

import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;

public interface UserSearchRepository extends ElasticsearchRepository<UserDocument, Long>,UserSearchRepositoryCustom  {
	@Query("{\"match\": {\"nickname\": {\"query\": \"?0\", \"analyzer\": \"nori\"}}}")
	Page<UserDocument> findByNicknameContaining(String keyword, Pageable pageable);
}
