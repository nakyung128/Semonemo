package com.semonemo.spring_server.domain.user.service;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semonemo.spring_server.domain.user.dto.request.UserUpdateRequestDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;
	private final FollowRepository followRepository;

	@Override
	@Transactional(readOnly = true)
	public Users findByAddress(String address) {
		return userRepository.findByAddress(address)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
	}

	@Override
	@Transactional(readOnly = true)
	public Users findById(long userId) {
		return userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
	}

	@Override
	@Transactional
	public void updateUser(String address, UserUpdateRequestDTO requestDTO) {
		Users user = userRepository.findByAddress(address)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
		user.modify(requestDTO.getNickname(), requestDTO.getProfileImage());
	}

	@Override
	@Transactional
	public void deleteUser(String address) {
		Users user = userRepository.findByAddress(address)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
		userRepository.delete(user);
	}

	@Override
	@Transactional
	public void followUser(String fromUserAddress, long toUserId) {
		Users fromUser = getUserFromAddress(fromUserAddress);
		Users toUser = getUserFromId(toUserId);

		// 자신을 구독할 경우, 예외 처리
		if(fromUser.equals(toUser)) {
			throw new CustomException(ErrorCode.SELF_FOLLOW_ERROR);
		}

		// 이미 구독한 사용자인 경우, 예외 처리
		if(followRepository.existsByFromUserAndToUser(fromUser, toUser)) {
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
		if(fromUser.equals(toUser)) {
			throw new CustomException(ErrorCode.SELF_FOLLOW_ERROR);
		}

		// 구독 정보가 없는 경우, 예외 처리
		if(!followRepository.existsByFromUserAndToUser(fromUser, toUser)) {
			throw new CustomException(ErrorCode.NOT_FOLLOW_ERROR);
		}

		Follow follow = followRepository.findByFromUserAndToUser(fromUser, toUser);
		followRepository.delete(follow);
	}
}
