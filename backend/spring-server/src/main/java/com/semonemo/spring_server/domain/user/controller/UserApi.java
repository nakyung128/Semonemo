package com.semonemo.spring_server.domain.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import com.semonemo.spring_server.domain.user.dto.request.UserUpdateRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.exception.ErrorResponse;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;

@Tag(name = "사용자", description = "사용자 관련 API")
public interface UserApi {

	@Operation(summary = "사용자 자신의 정보 조회 API", description = "로그인되어 있는 사용자의 정보를 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
		@ApiResponse(responseCode = "401", description = "조회 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<UserInfoResponseDTO> getMyInfo(@AuthenticationPrincipal UserDetails userDetails);

	@Operation(summary = "타사용자 정보 조회 API", description = "타사용자의 정보를 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "조회 성공",
			content = @Content(schema = @Schema(implementation = UserInfoResponseDTO.class))),
		@ApiResponse(responseCode = "401", description = "조회 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<UserInfoResponseDTO> getUserInfo(@PathVariable long userId);

	@Operation(summary = "사용자 정보 수정 API", description = "사용자의 정보를 수정하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "수정 성공"),
		@ApiResponse(responseCode = "401", description = "수정 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Void> updateUser(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart MultipartFile file,
		@RequestPart UserUpdateRequestDTO requestDTO) throws IOException;

	@Operation(summary = "사용자 탈퇴 API", description = "사용자의 정보를 삭제하는(회원 탈퇴) API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "삭제 성공"),
		@ApiResponse(responseCode = "401", description = "삭제 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails);

	@Operation(summary = "사용자 팔로우 API", description = "특정 사용자를 팔로우하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "팔로우 성공"),
		@ApiResponse(responseCode = "401", description = "팔로우 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Void> followUser(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable long userId
	);

	@Operation(summary = "사용자 언팔로우 API", description = "특정 사용자를 언팔로우하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "언팔로우 성공"),
		@ApiResponse(responseCode = "401", description = "언팔로우 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Void> unfollowUser(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable long userId
	);

	@Operation(summary = "사용자 팔로우 확인 API", description = "특정 사용자 팔로우 여부를 확인하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "팔로우 확인 성공"),
		@ApiResponse(responseCode = "401", description = "팔로우 확인 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<Boolean> checkFollow(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable long userId
	);

	@Operation(summary = "팔로잉 목록 조회 API", description = "특정 사용자의 팔로잉을 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "팔로잉 조회 성공",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserInfoResponseDTO.class)))),
		@ApiResponse(responseCode = "401", description = "팔로잉 조회 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<List<UserInfoResponseDTO>> getFollowing(long userId);

	@Operation(summary = "팔로워 목록 조회 API", description = "특정 사용자의 팔로워를 조회하는 API")
	@ApiResponses(value = {
		@ApiResponse(responseCode = "200", description = "팔로워 조회 성공",
			content = @Content(array = @ArraySchema(schema = @Schema(implementation = UserInfoResponseDTO.class)))),
		@ApiResponse(responseCode = "401", description = "팔로워 조회 실패",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "404", description = "사용자를 찾을 수 없음",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
		@ApiResponse(responseCode = "500", description = "서버 내부 오류",
			content = @Content(schema = @Schema(implementation = ErrorResponse.class))),
	})
	CommonResponse<List<UserInfoResponseDTO>> getFollowers(long userId);
}
