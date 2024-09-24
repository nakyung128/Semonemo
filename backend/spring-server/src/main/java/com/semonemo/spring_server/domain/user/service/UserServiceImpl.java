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
		user.modify(requestDTO.nickname(), requestDTO.profileImage());
	}

	@Override
	@Transactional
	public void deleteUser(String address) {
		Users user = userRepository.findByAddress(address)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
		userRepository.delete(user);
	}
}
