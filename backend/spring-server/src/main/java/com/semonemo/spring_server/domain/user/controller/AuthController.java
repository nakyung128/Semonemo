package com.semonemo.spring_server.domain.user.controller;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.semonemo.spring_server.domain.user.dto.UserRegisterDTO;
import com.semonemo.spring_server.domain.user.service.AuthService;
import com.semonemo.spring_server.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

	private final AuthService authService;

	@Override
	@PostMapping("/register")
	public CommonResponse<Void> registerUser(@RequestBody UserRegisterDTO requestDTO) {
		authService.registerUser(requestDTO);
		return CommonResponse.success("회원가입에 성공했습니다.");
	}
}
