package com.semonemo.spring_server.domain.user.service;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.user.dto.request.UserUpdateRequestDTO;
import com.semonemo.spring_server.domain.user.entity.Follow;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.FollowRepository;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;
	private final ElasticsearchSyncService syncService;

	@Override
	@Transactional(readOnly = true)
	public Users findByAddress(String address) {
		return getUserFromAddress(address);
	}

	@Override
	@Transactional(readOnly = true)
	public Users findById(long userId) {
		return getUserFromId(userId);
	}

	@Override
	@Transactional
	public void updateUser(String address, UserUpdateRequestDTO requestDTO) {
		Users user = getUserFromAddress(address);

		if (requestDTO.getProfileImage() == null) {
			user.modify(requestDTO.getNickname());
		} else {
			user.modify(requestDTO.getNickname(), requestDTO.getProfileImage());
		}
		syncService.updateUser(user);

	}

	@Override
	@Transactional
	public void deleteUser(String address) {
		Users user = getUserFromAddress(address);
		userRepository.delete(user);
		syncService.deleteUser(user);
	}

	@Override
	@Transactional
	public void followUser(String fromUserAddress, long toUserId) {
		Users fromUser = getUserFromAddress(fromUserAddress);
		Users toUser = getUserFromId(toUserId);

		// 자신을 구독할 경우, 예외 처리
		if (fromUser.equals(toUser)) {
			throw new CustomException(ErrorCode.SELF_FOLLOW_ERROR);
		}

		// 이미 구독한 사용자인 경우, 예외 처리
		if (followRepository.existsByFromUserAndToUser(fromUser, toUser)) {
			throw new CustomException(ErrorCode.ALREADY_FOLLOW_ERROR);
		}

		Follow follow = Follow.builder()
			.fromUser(fromUser)
			.toUser(toUser)
			.build();
		followRepository.save(follow);
	}

	@Override
	@Transactional
	public void unfollowUser(String fromUserAddress, long toUserId) {
		Users fromUser = getUserFromAddress(fromUserAddress);
		Users toUser = getUserFromId(toUserId);

		// 자신을 구독할 경우, 예외 처리
		if (fromUser.equals(toUser)) {
			throw new CustomException(ErrorCode.SELF_FOLLOW_ERROR);
		}

		// 구독 정보가 없는 경우, 예외 처리
		if (!followRepository.existsByFromUserAndToUser(fromUser, toUser)) {
			throw new CustomException(ErrorCode.NOT_FOLLOW_ERROR);
		}

		Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser);
		followRepository.delete(follow);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean isFollowed(String fromUserAddress, long toUserId) {
		Users fromUser = getUserFromAddress(fromUserAddress);
		Users toUser = getUserFromId(toUserId);
		// 자신을 확인할 경우, 예외 처리
		if (fromUser.equals(toUser)) {
			throw new CustomException(ErrorCode.CHECK_SELF_FOLLOW_ERROR);
		}

		return followRepository.existsByFromUserAndToUser(fromUser, toUser);
	}

	@Override
	public List<Users> findFollowing(long userId) {
		Users user = getUserFromId(userId);
		return followRepository.findAllByFromUser(user).stream()
			.map(Follow::getToUser)
			.toList();
	}

	@Override
	public List<Users> findFollowers(long userId) {
		Users user = getUserFromId(userId);
		return followRepository.findAllByToUser(user).stream()
			.map(Follow::getFromUser)
			.toList();
	}

	private Users getUserFromAddress(String address) {
		return userRepository.findByAddress(address)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
	}

	private Users getUserFromId(long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
	}
}
