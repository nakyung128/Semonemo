package com.semonemo.spring_server.domain.user.service;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.common.JwtProvider;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

@ExtendWith(MockitoExtension.class)
class AuthServiceTest {

	@Mock
	private JwtProvider jwtProvider;

	@Mock
	private UserRepository userRepository;

	@Mock
	private PasswordEncoder passwordEncoder;

	@Mock
	private AuthenticationManager authenticationManager;

	@InjectMocks
	private AuthServiceImpl authService;

	@Test
	@DisplayName("회원가입 성공 테스트")
	void testRegisterUser_Success() {
		// Given
		UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO(
			"testAddress",
			"testPassword",
			"testNickname",
			"testProfileImage"
		);
		Users user = requestDTO.toEntity();

		// Mock 개체 동작 정의
		when(passwordEncoder.encode(anyString())).thenReturn("encodedPassword");
		when(userRepository.save(any(Users.class))).thenReturn(user);

		// When
		authService.registerUser(requestDTO);

		// Then
		verify(userRepository).save(any(Users.class));
		verify(passwordEncoder).encode(anyString());
	}

	@Test
	@DisplayName("회원가입 실패 테스트 - 이미 존재하는 주소")
	void testRegisterUser_Fail_ExistsByAddress() {
		// Given
		UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO(
			"testAddress",
			"testPassword",
			"testNickname",
			"testProfileImage"
		);

		// Mock 개체 동작 정의
		when(userRepository.existsByAddress(anyString())).thenReturn(true);

		// When
		assertEquals(ErrorCode.EXISTS_ADDRESS_ERROR,
			assertThrows(CustomException.class, () -> authService.registerUser(requestDTO)).getErrorCode());

		// Then
		verify(userRepository).existsByAddress(anyString());
	}

	@Test
	@DisplayName("회원가입 실패 테스트 - 유효하지 않은 값이 입력되었을 때")
	void testRegisterUser_InvalidData() {
		// Given
		UserRegisterRequestDTO requestDTO = new UserRegisterRequestDTO("", "", "", "");

		// When & Then
		assertEquals(ErrorCode.INVALID_USER_DATA_ERROR,
			assertThrows(CustomException.class, () -> authService.registerUser(requestDTO)).getErrorCode());
	}

	@Test
	@DisplayName("존재하는 주소 테스트 - 주소가 존재하는 경우")
	void testExistsByAddress_True() {
		// Given
		String address = "testAddress";
		when(userRepository.existsByAddress(address)).thenReturn(true);

		// When
		boolean result = authService.existsByAddress(address);

		// Then
		assertTrue(result);
		verify(userRepository).existsByAddress(address);
	}

	@Test
	@DisplayName("유효한 닉네임 테스트")
	void testExistsByNickname_ValidNickname() {
		// Given
		String nickname = "validNickname";
		when(userRepository.existsByNickname(nickname)).thenReturn(true);

		// When
		boolean result = authService.existsByNickname(nickname);

		// Then
		assertTrue(result);
		verify(userRepository).existsByNickname(nickname);
	}

	@Test
	@DisplayName("유효하지 않은 닉네임 테스트 - 특수문자가 포함된 경우")
	void testExistsByNickname_InvalidSpecialSymbolNickname() {
		// Given
		String invalidNickname = "invalid@Nickname";

		// When & Then
		assertEquals(ErrorCode.INVALID_NICKNAME_ERROR,
			assertThrows(CustomException.class, () -> authService.existsByNickname(invalidNickname)).getErrorCode());
	}

	@Test
	@DisplayName("유효하지 않은 닉네임 테스트 - 닉네임의 허용 길이를 초과한 경우")
	void testExistsByNickname_InvalidLengthNickname() {
		// Given
		String invalidNickname = "invalidNicknameInvalidNicknameInvalidNickname";

		// When & Then
		assertEquals(ErrorCode.INVALID_NICKNAME_ERROR,
			assertThrows(CustomException.class, () -> authService.existsByNickname(invalidNickname)).getErrorCode());
	}

	@Test
	@DisplayName("로그인 성공 테스트")
	void testLogin_Success() {
		// Given
		String address = "testAddress";
		String password = "password";

		Authentication mockAuthentication = mock(Authentication.class);
		when(authenticationManager.authenticate(any(UsernamePasswordAuthenticationToken.class)))
			.thenReturn(mockAuthentication);

		// When
		authService.login(address, password);

		// Then
		verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
		assertNotNull(SecurityContextHolder.getContext().getAuthentication());
	}

	@Test
	@DisplayName("토큰 생성 테스트")
	void testGenerateUserToken() {
		// Given
		String address = "testAddress";
		when(jwtProvider.generateAccessToken(address)).thenReturn("accessToken");
		when(jwtProvider.generateRefreshToken(address)).thenReturn("refreshToken");

		// When
		UserLoginResponseDTO result = authService.generateUserToken(address);

		// Then
		assertEquals("Bearer accessToken", result.accessToken());
		assertEquals("Bearer refreshToken", result.refreshToken());
		verify(jwtProvider).generateAccessToken(address);
		verify(jwtProvider).generateRefreshToken(address);
	}
}