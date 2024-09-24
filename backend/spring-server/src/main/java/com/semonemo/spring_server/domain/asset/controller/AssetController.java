package com.semonemo.spring_server.domain.asset.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.semonemo.spring_server.config.s3.S3Service;
import com.semonemo.spring_server.domain.asset.dto.AssetRequestDto;
import com.semonemo.spring_server.domain.asset.dto.AssetResponseDto;
import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.service.AssetService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/asset")
@RequiredArgsConstructor
public class AssetController {
	@Autowired
	private S3Service s3Service;
	@Autowired
	private AssetService assetService;
	@Autowired
	private UserService userService;

	@PostMapping
	public AssetRequestDto upload(
		@RequestPart(value = "file", required = true) MultipartFile file)
	{
		try {
			System.out.println("origin file "+file.toString());
			String extension = file.getOriginalFilename()
					.substring(file.getOriginalFilename().lastIndexOf("."));
			String fileUrl = s3Service.upload(file.getOriginalFilename(), file, extension);
			System.out.println("controller"+" "+fileUrl);
			AssetRequestDto assetRequestDto = new AssetRequestDto();
			assetRequestDto.setCreator(1L);
			assetRequestDto.setImageUrl(fileUrl);
			assetService.saveImage(assetRequestDto);
			return assetRequestDto;
		}

		catch (Exception e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}
	//에셋 상세 조히
	@GetMapping("/detail")
	public CommonResponse <AssetResponseDto>  getAssetDetail(@RequestParam Long assetId) {
		AssetResponseDto asset = assetService.getAssetDetail(assetId);
		return CommonResponse.success(asset,"에셋 상세조회 성공");
	}
	//판매 에셋 상세 조히
	@GetMapping("/sell/detail")
	public CommonResponse <AssetSellResponseDto>  getAssetSellDetail(@RequestParam Long assetSellId) {
		AssetSellResponseDto asset = assetService.getAssetSellDetail(assetSellId);
		return CommonResponse.success(asset,"에셋 상세조회 성공");
	}

	//판매중인 모든 에셋
	@GetMapping
	public CommonResponse<CursorResult<AssetSellResponseDto>> getAllAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size

	){
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
		CursorResult<AssetSellResponseDto> result = assetService.getAllAsset(users.getId(), cursorId, size);
		return CommonResponse.success(result,"전체조회 성공");
		}
		catch (Exception e) {
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}
	}

	//보유중인 에셋 조회
	@GetMapping("/mine")
	public CommonResponse<?> getMyAsset(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size
	){
		try {
			Users users = userService.findByAddress(userDetails.getUsername());
			CursorResult<AssetResponseDto> result = assetService.getMyAsset(users.getId(),cursorId,size);
			return CommonResponse.success(result,"보유한 에셋 가져오기 성공");
		}
		catch (Exception e){
			throw new CustomException(ErrorCode.INTERNAL_SERVER_ERROR);
		}

	}


	//유저가 단 댓글 목록 조회
	// @GetMapping("/comment/user")
	// public ApiResponse<?> getUserComment(@RequestParam Long commentWriterId) {
	// 	try {
	// 		List<CommentResponseDto> commentResponseDtos = boardService.getUserComment(commentWriterId);
	// 		return ApiResponse.createSuccess(commentResponseDtos, "댓글 유저별 조회에 성공하였습니다");
	// 	} catch (Exception e) {
	// 		return ApiResponse.createError(ErrorCode.USER_NOT_FOUND);
	// 	}
	// }



}
