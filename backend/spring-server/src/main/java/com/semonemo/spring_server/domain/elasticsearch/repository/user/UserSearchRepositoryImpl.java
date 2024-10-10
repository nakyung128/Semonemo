package com.semonemo.spring_server.domain.elasticsearch.repository.user;

import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.document.Document;
import org.springframework.data.elasticsearch.core.mapping.IndexCoordinates;
import org.springframework.data.elasticsearch.core.query.UpdateQuery;

import com.semonemo.spring_server.domain.user.entity.Users;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class UserSearchRepositoryImpl implements UserSearchRepositoryCustom {

	private final ElasticsearchOperations elasticsearchOperations;

	@Override
	public void updateUser(Users user) {
		UpdateQuery updateQuery = UpdateQuery.builder(user.getId().toString())
			.withDocument(Document.create().append("nickname", user.getNickname()))
			.withDocument(Document.create().append("profileImageUrl", user.getProfileImage()))
			.build();

		try {
			elasticsearchOperations.update(updateQuery, IndexCoordinates.of("users"));
		} catch (Exception e) {
			// 로깅 또는 예외 처리
			throw new RuntimeException("Failed to update nftSell data: " + user.getId(), e);
		}
	}
}
