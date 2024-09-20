package com.semonemo.spring_server.domain.user.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.semonemo.spring_server.domain.user.dto.request.UserLoginRequestDTO;
import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.domain.user.service.AuthService;
import com.semonemo.spring_server.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

	private final AuthService authService;

	@Override
	@PostMapping("/login")
	public CommonResponse<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO requestDTO) {
		authService.login(requestDTO.address(), requestDTO.password());
		UserLoginResponseDTO responseDTO = authService.generateUserToken(requestDTO.address());
		return CommonResponse.success(responseDTO, "로그인에 성공했습니다.");
	}

	@Override
	@PostMapping("/register")
	public CommonResponse<Void> registerUser(@RequestBody UserRegisterRequestDTO requestDTO) {
		authService.registerUser(requestDTO);
		return CommonResponse.success("회원가입에 성공했습니다.");
	}

	@Override
	@GetMapping("/exist")
	public CommonResponse<Boolean> checkUserExistence(@RequestParam String address) {
		return CommonResponse.success(authService.existsByAddress(address), "가입 여부 확인에 성공했습니다.");
	}

	@Override
	@GetMapping("/validate")
	public CommonResponse<Boolean> checkNicknameExistence(@RequestParam String nickname) {
		return CommonResponse.success(authService.existsByNickname(nickname), "중복 확인에 성공했습니다.");
	}
}
