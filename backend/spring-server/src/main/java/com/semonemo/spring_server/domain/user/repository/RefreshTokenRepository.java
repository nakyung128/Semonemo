package com.semonemo.spring_server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.transaction.annotation.Transactional;

import com.semonemo.spring_server.domain.user.entity.RefreshToken;
import com.semonemo.spring_server.domain.user.entity.Users;

public interface RefreshTokenRepository extends JpaRepository<RefreshToken, Long> {

	Optional<RefreshToken> findByUser(Users user);
}
