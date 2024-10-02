package com.semonemo.spring_server.domain.user.service;

import java.util.Optional;
import java.util.regex.Pattern;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.domain.user.entity.RefreshToken;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.RefreshTokenRepository;
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
	private final RefreshTokenRepository refreshTokenRepository;
	private final PasswordEncoder passwordEncoder;
	private final AuthenticationManager authenticationManager;
	private final ElasticsearchSyncService syncService;

	@Override
	@Transactional(readOnly = false)
	public void registerUser(UserRegisterRequestDTO requestDTO) {
		if (!requestDTO.isValid()) {
			throw new CustomException(ErrorCode.INVALID_USER_DATA_ERROR);
		}

		if (existsByAddress(requestDTO.getAddress())) {
			throw new CustomException(ErrorCode.EXISTS_ADDRESS_ERROR);
		}

		Users user = requestDTO.toEntity();
		String encodedPassword = passwordEncoder.encode(user.getPassword());
		user.setPassword(encodedPassword);

		userRepository.save(user);
		syncService.syncUser(user);
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
	@Transactional
	public UserLoginResponseDTO generateUserToken(String address) {
		String accessToken = TOKEN_PREFIX + jwtProvider.generateAccessToken(address);
		String refreshToken = TOKEN_PREFIX + jwtProvider.generateRefreshToken(address);

		saveRefreshToken(address, refreshToken);

		return new UserLoginResponseDTO(accessToken, refreshToken);
	}

	@Override
	@Transactional
	public UserLoginResponseDTO regenerateToken(String refreshToken) {
		String token = refreshToken.split(" ")[1].trim();
		if(!jwtProvider.validateToken(token)) {
			throw new CustomException(ErrorCode.INVALID_TOKEN_ERROR);
		}

		String address = jwtProvider.getAddress(token);
		return generateUserToken(address);
	}

	private boolean isNicknameValid(String nickname) {
		if (nickname == null || nickname.trim().isEmpty()) {
			return false;
		}
		return NICKNAME_PATTERN.matcher(nickname).matches();
	}

	@Transactional
	protected void saveRefreshToken(String address, String refreshToken) {
		Users user = userRepository.findByAddress(address)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		Optional<RefreshToken> existingRefreshToken = refreshTokenRepository.findByUser(user);

		if (existingRefreshToken.isPresent()) {
			// 기존 토큰이 있다면 새 토큰으로 업데이트
			RefreshToken tokenToUpdate = existingRefreshToken.get();
			tokenToUpdate.modify(refreshToken);
		} else {
			// 기존 토큰이 없다면 새로 생성
			RefreshToken newRefreshToken = RefreshToken.builder()
				.refreshToken(refreshToken)
				.user(user)
				.build();
			refreshTokenRepository.save(newRefreshToken);
		}
	}
}
