package com.semonemo.spring_server.domain.user.controller;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.AuthService;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.JwtProvider;

@WebMvcTest(UserController.class)
class UserControllerTest {

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	@MockBean
	private UserService userService;

	@MockBean
	private JwtProvider jwtProvider;

	@Test
	@WithMockUser(username = "testAddress")
	@DisplayName("자신의 정보 조회 API 테스트")
	void test_getMyInfo() throws Exception {
		// Given
		Users mockUser = Users.builder()
			.id(1L)
			.address("testAddress")
			.nickname("testNickname")
			.password("testPassword")
			.profileImage("testProfile")
			.build();
		when(userService.findByAddress("testAddress")).thenReturn(mockUser);

		// When & Then
		mockMvc.perform(get("/api/user/me"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.address").value("testAddress"))
			.andExpect(jsonPath("$.data.nickname").value("testNickname"))
			.andExpect(jsonPath("$.message").value("사용자 정보 조회에 성공했습니다."));
		verify(userService).findByAddress("testAddress");
	}

	@Test
	@WithMockUser
	@DisplayName("타사용자의 정보 조회 API 테스트")
	void test_getUserInfo() throws Exception {
		// Given
		long userId = 1L;
		Users mockUser = Users.builder()
			.id(userId)
			.address("testAddress")
			.nickname("testNickname")
			.password("testPassword")
			.profileImage("testProfile")
			.build();
		when(userService.findById(userId)).thenReturn(mockUser);

		mockMvc.perform(get("/api/user/{userId}/detail", userId))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.address").value("testAddress"))
			.andExpect(jsonPath("$.data.nickname").value("testNickname"))
			.andExpect(jsonPath("$.message").value("사용자 정보 조회에 성공했습니다."));

		verify(userService).findById(userId);
	}
}