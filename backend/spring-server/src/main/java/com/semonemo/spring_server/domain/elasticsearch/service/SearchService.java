package com.semonemo.spring_server.domain.elasticsearch.service;

import java.util.List;

import org.springframework.data.domain.Page;

import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.NftSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.PopularSearchDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.UserSearchResponseDto;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.global.common.CursorResult;

public interface SearchService {

	Page<AssetSearchResponseDto> findOrderBy(Users users, String orderBy, String keyword, int page, int size);

	Page<UserSearchResponseDto> findUser( String keyword, int page, int size);

	Page<NftSearchResponseDto> findNft(Users users, String orderBy, String keyword, int page, int size);

	List<PopularSearchDto> getPopularSearches(int days, int size);

	// List<String> findPopularSearches(int size);
	//
	// List<String> findPopularSearchesByTimeRange(int size, int days);

}
