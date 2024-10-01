package com.semonemo.spring_server.domain.elasticsearch.service;

import org.springframework.data.domain.Page;

import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.NftSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.UserSearchResponseDto;
import com.semonemo.spring_server.global.common.CursorResult;

public interface SearchService {

	CursorResult<AssetSearchResponseDto> searchAsset(Long nowid, String keyword, Long page, int size);

	Page<AssetSearchResponseDto> findOrderBy(Long nowid, String orderBy, String keyword, int page, int size);

	Page<UserSearchResponseDto> findUser(Long nowid, String keyword, int page, int size);

	Page<NftSearchResponseDto> findNft(Long nowid, String orderBy, String keyword, int page, int size);
}
