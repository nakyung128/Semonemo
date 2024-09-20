package com.semonemo.spring_server.domain.user.controller;

import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import com.semonemo.spring_server.domain.user.dto.request.UserLoginRequestDTO;
import com.semonemo.spring_server.domain.user.dto.request.UserRegisterRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserLoginResponseDTO;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자 인증", description = "사용자 인증 관련 API")
public interface AuthApi {

	@Operation(summary = "사용자 로그인 API", description = "사용자의 로그인을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "로그인 성공"),
		@ApiResponse(responseCode = "401", description = "로그인 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<UserLoginResponseDTO> login(@RequestBody UserLoginRequestDTO requestDTO);

	@Operation(summary = "사용자 회원가입 API", description = "신규 사용자의 회원가입을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "회원가입 성공"),
		@ApiResponse(responseCode = "400", description = "회원가입 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Void> registerUser(@RequestBody UserRegisterRequestDTO requestDTO);

	@Operation(summary = "사용자 가입 여부 확인 API", description = "지갑주소의 가입여부 확인을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "가입 여부 확인 성공"),
		@ApiResponse(responseCode = "400", description = "가입 여부 확인 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Boolean> checkUserExistence(@RequestParam String address);

	@Operation(summary = "닉네임 중복 확인 API", description = "닉네임의 중복 확인을 위한 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "닉네임 중복 확인 성공"),
		@ApiResponse(responseCode = "400", description = "닉네임 중복 확인 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Boolean> checkNicknameExistence(@RequestParam String nickname);
}
