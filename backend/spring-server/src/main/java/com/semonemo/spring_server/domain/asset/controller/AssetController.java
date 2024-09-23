package com.semonemo.spring_server.domain.asset.controller;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.semonemo.spring_server.domain.asset.service.AssetService;
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
	@GetMapping("/detail")
	public CommonResponse <AssetResponseDto>  getAssetDetail(@RequestParam Long assetId) {
		AssetResponseDto asset = assetService.getAssetDetail(assetId);
		return CommonResponse.success(asset,"에셋 상세조회 성공");
	}

	//판매중인 모든 에셋
	@GetMapping
	public CursorResult<AssetSellResponseDto> getAllAsset(
		@RequestParam(required = false) Long id,
		@RequestParam(required = false) Long cursorId,
		@RequestParam(defaultValue = "40") int size

	){
		return assetService.getAllAsset(id,cursorId,size);
	}



}
