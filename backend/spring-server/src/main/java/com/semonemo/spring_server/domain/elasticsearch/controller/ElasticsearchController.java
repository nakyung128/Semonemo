package com.semonemo.spring_server.domain.elasticsearch.controller;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.NftSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.PopularSearchDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.UserSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.service.SearchService;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.service.UserService;
import com.semonemo.spring_server.global.common.CommonResponse;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ElasticsearchController implements ElasticSearchApi {
	private RestTemplate restTemplate;
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
		sendSearchQuery(keyword);
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
		sendSearchQuery(keyword);
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
		sendSearchQuery(keyword);
		return CommonResponse.success(result, "NFT 키워드 검색 성공");
	}

	private void sendSearchQuery(String keyword) {
		RestTemplate restTemplate = new RestTemplate();

		String logMessage = String.format("%s INFO [SearchService] Search query: %s",
			LocalDateTime.now(), keyword);
		System.out.println("Sending log message: " + logMessage);

		HttpHeaders headers = new HttpHeaders();
		headers.setContentType(MediaType.APPLICATION_JSON);
		Map<String, String> body = new HashMap<>();
		body.put("message", logMessage);
		body.put("search_type", "nft");

		HttpEntity<Map<String, String>> request = new HttpEntity<>(body, headers);

		try {
			ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:5000", request,
				String.class);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@GetMapping("/hot")
	public CommonResponse<?> getPopularSearches(
		@RequestParam(defaultValue = "7") int days,
		@RequestParam(defaultValue = "10") int size) {
		List<PopularSearchDto> popularSearches = searchService.getPopularSearches(days, size);
		return CommonResponse.success(popularSearches, "인기 검색어 조회 성공");
	}
}
