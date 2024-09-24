package com.semonemo.spring_server.domain.user.service;

import java.util.List;

import com.semonemo.spring_server.domain.user.dto.request.UserUpdateRequestDTO;
import com.semonemo.spring_server.domain.user.entity.Users;

public interface UserService {

	Users findByAddress(String address);

	Users findById(long userId);

	void updateUser(String address, UserUpdateRequestDTO requestDTO);

	void deleteUser(String address);

	void followUser(String fromUserAddress, long toUserId);

	void unfollowUser(String fromUserAddress, long toUserId);

	boolean isFollowed(String fromUserAddress, long toUserId);

	List<Users> findFollowing(long userId);

	List<Users> findFollowers(long userId);
}
