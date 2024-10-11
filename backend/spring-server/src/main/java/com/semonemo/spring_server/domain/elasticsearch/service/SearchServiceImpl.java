package com.semonemo.spring_server.domain.elasticsearch.service;

import java.math.BigInteger;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.asset.repository.like.AssetLikeRepository;
import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;
import com.semonemo.spring_server.domain.elasticsearch.document.NFTSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.document.UserDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.AssetSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.document.AssetSellDocument;
import com.semonemo.spring_server.domain.elasticsearch.dto.NftSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.PopularSearchDto;
import com.semonemo.spring_server.domain.elasticsearch.dto.UserSearchResponseDto;
import com.semonemo.spring_server.domain.elasticsearch.repository.nft.NftSearchRepository;
import com.semonemo.spring_server.domain.elasticsearch.repository.asset.AssetElasticsearchRepository;
import com.semonemo.spring_server.domain.elasticsearch.repository.searchquery.SearchQueryRepository;
import com.semonemo.spring_server.domain.elasticsearch.repository.user.UserSearchRepository;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.repository.nftmarket.NFTMarketRepository;
import com.semonemo.spring_server.domain.nft.repository.nftmarketlike.NFTMarketLikeRepository;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class SearchServiceImpl implements SearchService {
	private final AssetElasticsearchRepository assetElasticsearchRepository;
	private final UserSearchRepository userSearchRepository;
	private final AssetLikeRepository assetLikeRepository;
	private final NFTMarketLikeRepository nftMarketLikeRepository;
	private final NFTMarketRepository nftMarketRepository;
	private final NftSearchRepository nftSearchRepository;
	private final SearchQueryRepository searchQueryRepository;
	private final BlockChainService blockChainService;
	private final UserRepository userRepository;



	@Override
	public Page<AssetSearchResponseDto> findOrderBy(Users users, String orderBy, String keyword, int page, int size) {
		Page<AssetSellDocument> assetSellDocuments = assetElasticsearchRepository.keywordAndOrderby(keyword, orderBy,
			page, size);

		List<AssetSearchResponseDto> assetSellDtos = assetSellDocuments.getContent().stream()
			.map(document -> convertToAssetDto(users, document))
			.toList();

		return new PageImpl<>(assetSellDtos, assetSellDocuments.getPageable(), assetSellDocuments.getTotalElements());
	}

	@Override
	public Page<NftSearchResponseDto> findNft(Users users, String orderBy, String keyword, int page, int size) {
		Page<NFTSellDocument> nftSellDocuments = nftSearchRepository.keywordAndOrderby(keyword, orderBy, page, size);
		List<NftSearchResponseDto> nftSearchResponseDtos = nftSellDocuments.getContent().stream()
			.map(nftSellDocument -> convertToNFTDto(users, nftSellDocument))
			.toList();
		return new PageImpl<>(nftSearchResponseDtos, nftSellDocuments.getPageable(),
			nftSellDocuments.getTotalElements());
	}

	@Override
	public List<PopularSearchDto> getPopularSearches(int days, int size) {
		return searchQueryRepository.getPopularSearches(days, size);
	}

	@Override
	public Page<UserSearchResponseDto> findUser( String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<UserDocument> userDocumentPage = userSearchRepository.findByNicknameContaining(keyword, pageable);
		List<UserSearchResponseDto> userSearchResponseDtos = userDocumentPage.getContent().stream()
			.map(this::convertToUserDto)
			.collect(Collectors.toList());

		return new PageImpl<>(userSearchResponseDtos, pageable, userDocumentPage.getTotalElements());
	}

	private AssetSearchResponseDto convertToAssetDto(Users users, AssetSellDocument document) {
		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(users.getId(), document.getAssetSellId());
		Users creator = userRepository.findById(document.getCreator())
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
		UserInfoResponseDTO userDto = convertToUserInfo(creator);
		AssetSearchResponseDto dto = new AssetSearchResponseDto(
			document.getAssetSellId(),
			document.getAssetId(),
			userDto,
			document.getImageUrls(),
			document.getPrice(),
			document.getHits(),
			document.getCreatedAt(),
			document.getLikeCount(),
			document.getTags().stream()
				.map(AssetSellDocument.Tag::getName)  // Tag 객체의 name 필드를 추출하여 List<String>으로 변환
				.collect(Collectors.toList()),
			isLiked,
			document.getPurchaseCount()
		);
		return dto;
	}

	private UserSearchResponseDto convertToUserDto(UserDocument document) {
		Users creator = userRepository.findById(document.getId())
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
		UserSearchResponseDto userDto = new UserSearchResponseDto(
			convertToUserInfo(creator)
		);
		return userDto;
	}

	private NftSearchResponseDto convertToNFTDto(Users users, NFTSellDocument document) {
		boolean isLiked = nftMarketLikeRepository.existsByUserIdAndMarketId(users.getId(), document.getNftSellId());
		NFTMarket market = nftMarketRepository.findById(document.getNftSellId())
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
		List<BigInteger> tokenIds = List.of(market.getNftId().getTokenId());
		List<NFTInfoDto> nftInfos;
		try {
			nftInfos = blockChainService.getNFTsByIds(tokenIds);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}
		NFTInfoDto nftinfo = nftInfos.get(0);
		Users creator = userRepository.findById(document.getCreator())
			.orElseThrow(()-> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));
		UserInfoResponseDTO userDto = convertToUserInfo(creator);
		NftSearchResponseDto nftDto = new NftSearchResponseDto(
			document.getNftSellId(),
			document.getNftId(),
			userDto,
			document.getPrice(),
			document.getLikeCount(),
			isLiked,
			nftinfo,
			document.getTags().stream()
				.map(NFTSellDocument.Tag::getName)  // Tag 객체의 name 필드를 추출하여 List<String>으로 변환
				.collect(Collectors.toList())
		);
		return nftDto;
	}
	private UserInfoResponseDTO convertToUserInfo (Users users) {
		UserInfoResponseDTO userDto = new UserInfoResponseDTO(
			users.getId(),
			users.getAddress(),
			users.getNickname(),
			users.getProfileImage()
		);
		return userDto;
	}
}
