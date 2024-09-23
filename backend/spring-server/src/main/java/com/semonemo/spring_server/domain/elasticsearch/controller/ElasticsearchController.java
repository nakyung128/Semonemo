package com.semonemo.spring_server.domain.elasticsearch.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.service.SearchService;
import com.semonemo.spring_server.global.common.CursorResult;

import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
public class ElasticsearchController {
	@Autowired
	private final SearchService searchService;
	@GetMapping("/asset")
	public CursorResult<AssetSellDocument> searchAsset(
		@RequestParam(required = false) Long nowid,
		@RequestParam(required = false) String keyword,
		@RequestParam(required = false) Long cursor ,
		@RequestParam(defaultValue = "10") int size) {
		CursorResult<AssetSellDocument> result =searchService.searchAsset(nowid,keyword,cursor,size);

		return searchService.searchAsset(nowid,keyword,cursor,size);
	}
	@GetMapping("/all")
	public AssetSellDocument findById(@RequestParam(required = false) Long id) {
		return searchService.getAsset(id);
	}

}
