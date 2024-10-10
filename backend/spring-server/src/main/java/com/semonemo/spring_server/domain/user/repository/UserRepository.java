package com.semonemo.spring_server.domain.user.repository;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.user.entity.Users;

public interface UserRepository extends JpaRepository<Users, Long> {

	Optional<Users> findByAddress(String address);

	boolean existsByAddress(String address);

	boolean existsByNickname(String nickname);
}
