package com.semonemo.spring_server.domain.elasticsearch.repository.user;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;
import com.semonemo.spring_server.domain.user.entity.Users;

public interface UserSearchRepositoryCustom {
	void updateUser(Users user);
	// Page<UserDocument> findByNicknameContaining(String keyword, Pageable pageable);
}
