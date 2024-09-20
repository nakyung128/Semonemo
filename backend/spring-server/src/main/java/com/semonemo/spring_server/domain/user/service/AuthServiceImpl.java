package com.semonemo.spring_server.domain.user.service;

import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.common.JwtProvider;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AuthServiceImpl implements AuthService {

	private static final String TOKEN_PREFIX = "Bearer ";
	private static final String NICKNAME_REGEX = "^[가-힣a-zA-Z0-9_]{1,15}$";
	private static final Pattern NICKNAME_PATTERN = Pattern.compile(NICKNAME_REGEX);

	private final JwtProvider jwtProvider;
	private final UserRepository userRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;

	@Override
	@Transactional(readOnly = false)
	public String registerUser(UserRegisterRequestDTO requestDTO) {
		if (!requestDTO.isValid()) {
			throw new CustomException(ErrorCode.INVALID_USER_DATA_ERROR);
		}

		Users user = requestDTO.toEntity();
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
		if (!isNicknameValid(nickname)) {
			throw new CustomException(ErrorCode.INVALID_NICKNAME_ERROR);
		}
		return userRepository.existsByNickname(nickname);
	}

	@Override
	public void login(String address, String password) {
		Authentication authentication = authenticationManager
			.authenticate(new UsernamePasswordAuthenticationToken(address, password));
		SecurityContextHolder.getContext().setAuthentication(authentication);
	}

	@Override
	public UserLoginResponseDTO generateUserToken(String address) {
		String accessToken = TOKEN_PREFIX + jwtProvider.generateAccessToken(address);
		String refreshToken = TOKEN_PREFIX + jwtProvider.generateRefreshToken(address);
		return new UserLoginResponseDTO(accessToken, refreshToken);
	}

	private boolean isNicknameValid(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return false;
		}
		return NICKNAME_PATTERN.matcher(nickname).matches();
	}
}
