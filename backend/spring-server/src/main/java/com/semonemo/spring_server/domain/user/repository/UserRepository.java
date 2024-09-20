package com.semonemo.spring_server.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.user.entity.User;

public interface UserRepository extends JpaRepository<User, Long> {
}
