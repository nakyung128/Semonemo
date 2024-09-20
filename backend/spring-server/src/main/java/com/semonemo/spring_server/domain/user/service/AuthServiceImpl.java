package com.semonemo.spring_server.domain.user.service;

import java.util.regex.Pattern;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semonemo.spring_server.domain.user.dto.UserRegisterDTO;
import com.semonemo.spring_server.domain.user.entity.User;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private static final String NICKNAME_REGEX = "^[가-힣a-zA-Z0-9_]{1,15}$";
	private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);

	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;

	@Override
	@Transactional(readOnly = false)
	public String registerUser(UserRegisterDTO requestDTO) {
		if (!requestDTO.isValid()) {
			throw new CustomException(ErrorCode.INVALID_USER_DATA_ERROR);
		}

		User user = requestDTO.toEntity();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		return userRepository.save(user).getNickname();
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByAddress(String address) {
		return userRepository.existsByAddress(address);
	}

	@Override
	@Transactional(readOnly = true)
	public boolean existsByNickname(String nickname) {
		if(!isNicknameValid(nickname)) {
			throw new CustomException(ErrorCode.INVALID_NICKNAME_ERROR);
		}
		return userRepository.existsByNickname(nickname);
	}

	private boolean isNicknameValid(String nickname) {
		if(nickname == null || nickname.trim().isEmpty()) {
			return false;
		}
		return NICKNAME_PATTERN.matcher(nickname).matches();
	}
}
