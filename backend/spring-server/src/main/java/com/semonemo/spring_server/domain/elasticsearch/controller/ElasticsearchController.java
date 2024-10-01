package com.semonemo.spring_server.domain.elasticsearch.controller;

import org.springframework.data.domain.Page;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.NftSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.UserSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.service.SearchService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;
import com.semonemo.spring_server.global.common.CursorResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ElasticsearchController implements ElasticSearchApi {

	private final SearchService searchService;
	private final UserService userService;
	@GetMapping("/users")
	public CommonResponse<?> searchUsers(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam String keyword,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "10") int size) {
		Users users = userService.findByAddress(userDetails.getUsername());
		Page<UserSearchResponseDto> result = searchService.findUser(users.getId(), keyword, page, size);

		return CommonResponse.success(result, "유저 키워드 검색 성공");
	}

	@GetMapping("/asset")
	public CommonResponse<?> searchAssetOrderBy(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "latest") String orderBy,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "40") int size) {
		Users users = userService.findByAddress(userDetails.getUsername());
		Page<AssetSearchResponseDto> result = searchService.findOrderBy(users.getId(), orderBy, keyword, page, size);

		return CommonResponse.success(result, "에셋 키워드 검색 성공");
	}

	@GetMapping("/nft")
	public CommonResponse<?> searchNft(
		@AuthenticationPrincipal UserDetails userDetails,
		@RequestParam(required = false) String keyword,
		@RequestParam(defaultValue = "latest") String orderBy,
		@RequestParam(defaultValue = "0") int page,
		@RequestParam(defaultValue = "40") int size) {
		Users users = userService.findByAddress(userDetails.getUsername());
		Page<NftSearchResponseDto> result = searchService.findNft(users.getId(), orderBy, keyword, page, size);

		return CommonResponse.success(result, "NFT 키워드 검색 성공");
	}
}
