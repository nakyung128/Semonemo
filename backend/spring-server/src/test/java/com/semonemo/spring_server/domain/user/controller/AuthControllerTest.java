package com.semonemo.spring_server.domain.user.controller;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.config.s3.S3Service;
import com.semonemo.spring_server.domain.user.dto.request.UserLoginRequestDTO;
import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.domain.user.service.AuthService;
import com.semonemo.spring_server.global.common.JwtProvider;

@WebMvcTest(
	controllers = AuthController.class,
	excludeAutoConfiguration = SecurityAutoConfiguration.class
)
class AuthControllerTest {

	private static final String SUCCESS_CODE = "S000";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private JwtProvider jwtProvider;

	@MockBean
	private AuthService authService;

	@MockBean
	private S3Service s3Service;

	@Test
	@DisplayName("로그인 API 테스트")
	void test_login() throws Exception {
		// Given
		UserLoginRequestDTO requestDTO = new UserLoginRequestDTO("testAddress", "password");
		UserLoginResponseDTO responseDTO = new UserLoginResponseDTO("accessToken", "refreshToken");

		when(authService.generateUserToken(anyString())).thenReturn(responseDTO);

		// When & Then
		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(requestDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(SUCCESS_CODE))
			.andExpect(jsonPath("$.data.accessToken").value("accessToken"))
			.andExpect(jsonPath("$.data.refreshToken").value("refreshToken"))
			.andExpect(jsonPath("$.message").value("로그인에 성공했습니다."));

		verify(authService).login(requestDTO.address(), requestDTO.password());
		verify(authService).generateUserToken(requestDTO.address());
	}

	@Test
	@DisplayName("회원 가입 API 테스트")
	void test_register() throws Exception {
		// Given
		UserRegisterRequestDTO requestDTO = UserRegisterRequestDTO.builder()
			.address("testAddress")
			.password("testPassword")
			.nickname("testNickname")
			.build();

		// When & Then
		MockMultipartFile file = new MockMultipartFile(
			"image",
			"test.jpg",
			MediaType.IMAGE_JPEG_VALUE,
			"test image content".getBytes()
		);

		String requestDTOJson = objectMapper.writeValueAsString(requestDTO);
		MockMultipartFile jsonFile = new MockMultipartFile(
			"data",
			"",
			"application/json",
			requestDTOJson.getBytes()
		);

		mockMvc.perform(multipart("/api/auth/register")
				.file(file)
				.file(jsonFile))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.message").value("회원가입에 성공했습니다."));

		verify(authService).registerUser(any(UserRegisterRequestDTO.class));
	}

	@Test
	@DisplayName("사용자 가입 여부 확인 API 테스트")
	void testCheckUserExistence() throws Exception {
		// Given
		String address = "testAddress";
		when(authService.existsByAddress(address)).thenReturn(true);

		// When & Then
		mockMvc.perform(get("/api/auth/exist")
				.param("address", address))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(true))
			.andExpect(jsonPath("$.message").value("가입 여부 확인에 성공했습니다."));

		verify(authService).existsByAddress(address);
	}

	@Test
	@DisplayName("사용자 닉네임 중복 확인 API 테스트")
	void testCheckNicknameExistence() throws Exception {
		// Given
		String nickname = "newNickname";
		when(authService.existsByNickname(nickname)).thenReturn(false);

		// When & Then
		mockMvc.perform(get("/api/auth/validate")
				.param("nickname", nickname))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(false))
			.andExpect(jsonPath("$.message").value("중복 확인에 성공했습니다."));

		verify(authService).existsByNickname(nickname);
	}
}