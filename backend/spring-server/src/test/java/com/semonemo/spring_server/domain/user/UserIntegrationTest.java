package com.semonemo.spring_server.domain.user;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.semonemo.spring_server.domain.user.dto.request.UserLoginRequestDTO;
import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;

@SpringBootTest
@AutoConfigureMockMvc
@Transactional
public class UserIntegrationTest {

	private static final String SUCCESS_CODE = "S000";

	@Autowired
	private MockMvc mockMvc;

	@Autowired
	private ObjectMapper objectMapper;

	private String accessToken;

	@BeforeEach
	void setUp() throws Exception {
		// 사용자 등록
		UserRegisterRequestDTO registerDTO = new UserRegisterRequestDTO(
			"testAddress",
			"testPassword",
			"testNickname",
			"testProfile"
		);
		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDTO)))
			.andExpect(status().isOk());

		// 로그인 및 토큰 획득
		UserLoginRequestDTO loginDTO = new UserLoginRequestDTO(
			"testAddress",
			"testPassword"
		);
		MvcResult result = mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO)))
			.andExpect(status().isOk())
			.andReturn();

		String content = result.getResponse().getContentAsString();
		accessToken = objectMapper.readTree(content).path("data").get("accessToken").asText();
	}

	@Test
	@DisplayName("회원 가입 통합 테스트")
	void test_register() throws Exception {
		UserRegisterRequestDTO registerDTO = new UserRegisterRequestDTO(
			"testAddress2",
			"testPassword",
			"testNickname",
			"testProfile"
		);

		mockMvc.perform(post("/api/auth/register")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(registerDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.code").value(SUCCESS_CODE))
			.andExpect(jsonPath("$.message").value("회원가입에 성공했습니다."));
	}

	@Test
	@DisplayName("로그인 통합 테스트")
	void test_login() throws Exception {
		UserLoginRequestDTO loginDTO = new UserLoginRequestDTO(
			"testAddress",
			"testPassword"
		);

		mockMvc.perform(post("/api/auth/login")
				.contentType(MediaType.APPLICATION_JSON)
				.content(objectMapper.writeValueAsString(loginDTO)))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.accessToken").exists())
			.andExpect(jsonPath("$.message").value("로그인에 성공했습니다."));
	}

	@Test
	@DisplayName("지갑 주소를 통해 사용자 존재 여부 통합 테스트")
	void test_checkUserExistence() throws Exception {
		mockMvc.perform(get("/api/auth/exist")
				.param("address", "testAddress"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(true))
			.andExpect(jsonPath("$.message").value("가입 여부 확인에 성공했습니다."));
	}

	@Test
	@DisplayName("닉네임 존재 여부 확인 통합 테스트")
	void test_checkNicknameExistence() throws Exception {
		mockMvc.perform(get("/api/auth/validate")
				.param("nickname", "testNickname"))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data").value(true))
			.andExpect(jsonPath("$.message").value("중복 확인에 성공했습니다."));
	}

	@Test
	@WithMockUser(username = "testAddress")
	@DisplayName("자신의 정보 조회 통합 테스트")
	void test_getMyInfo() throws Exception {
		mockMvc.perform(get("/api/user/me")
				.header("Authorization", accessToken))
			.andExpect(status().isOk())
			.andExpect(jsonPath("$.data.address").value("testAddress"))
			.andExpect(jsonPath("$.message").value("사용자 정보 조회에 성공했습니다."));
	}
}
