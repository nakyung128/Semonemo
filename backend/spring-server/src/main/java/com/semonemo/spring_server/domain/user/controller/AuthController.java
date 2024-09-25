package com.semonemo.spring_server.domain.user.controller;

import java.io.IOException;

import org.springframework.http.MediaType;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.semonemo.spring_server.config.s3.S3Service;
import com.semonemo.spring_server.domain.user.dto.request.UserLoginRequestDTO;
import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.domain.user.service.AuthService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController implements AuthApi {

	private final S3Service s3Service;
	private final AuthService authService;

	@Override
	@PostMapping("/login")
	public CommonResponse<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO requestDTO) {
		authService.login(requestDTO.address(), requestDTO.password());
		UserLoginResponseDTO responseDTO = authService.generateUserToken(requestDTO.address());
		return CommonResponse.success(responseDTO, "로그인에 성공했습니다.");
	}

	@Override
	@PostMapping(value = "/register")
	public CommonResponse<Void> registerUser(
		@RequestPart(value = "image", required = false) MultipartFile file,
		@RequestPart(value = "data") UserRegisterRequestDTO requestDTO
	) throws IOException {
		if (!file.isEmpty()) {
			requestDTO.setProfileImage(s3Service.upload(file));
		}
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

	@Override
	@GetMapping("/refresh-token")
	public CommonResponse<UserLoginResponseDTO> regenerateJWTToken(@RequestParam String refreshToken) {
		UserLoginResponseDTO responseDTO = authService.regenerateToken(refreshToken);
		return CommonResponse.success(responseDTO, "토큰 재발급에 성공했습니다.");
	}

}
