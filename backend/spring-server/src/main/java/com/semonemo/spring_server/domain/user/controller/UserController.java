package com.semonemo.spring_server.domain.user.controller;

import java.io.IOException;
import java.util.List;

import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.semonemo.spring_server.config.s3.S3Service;
import com.semonemo.spring_server.domain.user.dto.request.UserUpdateRequestDTO;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/user")
public class UserController implements UserApi {

	private final S3Service s3Service;
	private final UserService userService;

	@Override
	@GetMapping("/me")
	public CommonResponse<UserInfoResponseDTO> getMyInfo(@AuthenticationPrincipal UserDetails userDetails) {
		Users user = userService.findByAddress(userDetails.getUsername());
		UserInfoResponseDTO responseDTO = UserInfoResponseDTO.fromEntity(user);
		return CommonResponse.success(responseDTO, "사용자 정보 조회에 성공했습니다.");
	}

	@Override
	@GetMapping("/{userId}/detail")
	public CommonResponse<UserInfoResponseDTO> getUserInfo(@PathVariable long userId) {
		Users user = userService.findById(userId);
		UserInfoResponseDTO responseDTO = UserInfoResponseDTO.fromEntity(user);
		return CommonResponse.success(responseDTO, "사용자 정보 조회에 성공했습니다.");
	}

	@Override
	@PutMapping
	public CommonResponse<Void> updateUser(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestPart(value = "image", required = false) MultipartFile file,
		@RequestPart(value = "data") UserUpdateRequestDTO requestDTO
	) throws IOException {
		if (!file.isEmpty()) {
			requestDTO.setProfileImage(s3Service.upload(file));
		}
		userService.updateUser(userDetails.getUsername(), requestDTO);
		return CommonResponse.success("사용자 정보 수정에 성공했습니다.");
	}

	@Override
	@DeleteMapping
	public CommonResponse<Void> deleteUser(@AuthenticationPrincipal UserDetails userDetails) {
		userService.deleteUser(userDetails.getUsername());
		return CommonResponse.success("사용자 탈퇴에 성공했습니다.");
	}

	@Override
	@PostMapping("/{userId}/follow")
	public CommonResponse<Void> followUser(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable long userId
	) {
		userService.followUser(userDetails.getUsername(), userId);
		return CommonResponse.success("팔로우에 성공했습니다.");
	}

	@Override
	@DeleteMapping("/{userId}/follow")
	public CommonResponse<Void> unfollowUser(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable long userId
	) {
		userService.unfollowUser(userDetails.getUsername(), userId);
		return CommonResponse.success("언팔로우에 성공했습니다.");
	}

	@Override
	@GetMapping("/{userId}/follow")
	public CommonResponse<Boolean> checkFollow(
		@AuthenticationPrincipal UserDetails userDetails,
		@PathVariable long userId
	) {
		boolean result = userService.isFollowed(userDetails.getUsername(), userId);
		return CommonResponse.success(result, "팔로우 여부 확인에 성공했습니다.");
	}

	@Override
	@GetMapping("/{userId}/following")
	public CommonResponse<List<UserInfoResponseDTO>> getFollowing(@PathVariable long userId) {
		List<Users> following = userService.findFollowing(userId);

		List<UserInfoResponseDTO> responseDTO = following.stream()
			.map(UserInfoResponseDTO::fromEntity)
			.toList();
		return CommonResponse.success(responseDTO, "팔로잉 목록 조회에 성공했습니다.");
	}

	@Override
	@GetMapping("/{userId}/followers")
	public CommonResponse<List<UserInfoResponseDTO>> getFollowers(@PathVariable long userId) {
		List<Users> following = userService.findFollowers(userId);

		List<UserInfoResponseDTO> responseDTO = following.stream()
			.map(UserInfoResponseDTO::fromEntity)
			.toList();
		return CommonResponse.success(responseDTO, "팔로워 목록 조회에 성공했습니다.");
	}
}
