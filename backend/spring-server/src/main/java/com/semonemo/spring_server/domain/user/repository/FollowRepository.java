package com.semonemo.spring_server.domain.user.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.user.entity.Follow;
import com.semonemo.spring_server.domain.user.entity.Users;

public interface FollowRepository extends JpaRepository<Follow, Long> {

	boolean existsByFromUserAndToUser(Users fromUser, Users toUser);

	Follow findByFromUserAndToUser(Users fromUser, Users toUser);

	List<Follow> findAllByFromUser(Users fromUser);

	List<Follow> findAllByToUser(Users toUser);
}
