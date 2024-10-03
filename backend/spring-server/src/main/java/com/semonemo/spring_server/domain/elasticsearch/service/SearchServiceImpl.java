package com.semonemo.spring_server.domain.elasticsearch.service;

import java.io.IOException;
import java.math.BigInteger;
import java.time.LocalDate;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
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
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.aggregations.StringTermsBucket;
import co.elastic.clients.elasticsearch.core.SearchResponse;
import co.elastic.clients.json.JsonData;
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
	private final ElasticsearchOperations elasticsearchOperations;
	private final ElasticsearchClient elasticsearchClient;
	private  final ElasticsearchSyncService syncService;
	private final BlockChainService blockChainService;



	@PostConstruct
	public void initializeElasticsearch() {
		syncService.syncAllData();
	}
	@Override
	public CursorResult<AssetSearchResponseDto> searchAsset(Long nowid, String keyword, Long cursorId, int size) {
		CursorResult<AssetSellDocument> assetSellDocument = assetElasticsearchRepository.findByTagKeyword(keyword, cursorId, size);
		List<AssetSearchResponseDto> dtos = assetSellDocument.getContent().stream()
			.map(document -> convertToAssetDto(nowid, document))
			.toList();
		return new CursorResult<>(dtos,assetSellDocument.getNextCursor(),assetSellDocument.isHasNext());
	}

	@Override
	public Page<AssetSearchResponseDto> findOrderBy(Long nowid, String orderBy, String keyword, int page, int size) {
		Page<AssetSellDocument> assetSellDocuments = assetElasticsearchRepository.keywordAndOrderby( keyword, orderBy, page, size);

		List<AssetSearchResponseDto> assetSellDtos = assetSellDocuments.getContent().stream()
			.map(document -> convertToAssetDto(nowid, document))
			.toList();

		return new PageImpl<>(assetSellDtos, assetSellDocuments.getPageable(), assetSellDocuments.getTotalElements());
	}


	@Override
	public Page<NftSearchResponseDto> findNft(Long nowid, String orderBy, String keyword, int page, int size) {
		Page<NFTSellDocument> nftSellDocuments = nftSearchRepository.keywordAndOrderby(keyword,orderBy,page,size);
		List<NftSearchResponseDto> nftSearchResponseDtos = nftSellDocuments.getContent().stream()
			.map(nftSellDocument -> convertToNFTDto(nowid,nftSellDocument) )
			.toList();
		return new PageImpl<>(nftSearchResponseDtos,nftSellDocuments.getPageable(),nftSellDocuments.getTotalElements());
	}

	@Override
	public List<PopularSearchDto> getPopularSearches(int days, int size) {
		try {
			SearchResponse<Void> response = elasticsearchClient.search(s -> s
					.index("search_queries-*")
					.query(q -> q
						.range(r -> r
							.field("@timestamp")
							.gte(JsonData.of(LocalDate.now().minusDays(days).atStartOfDay(ZoneOffset.UTC).toInstant().toString())) // UTC 시간으로 변환
							.lte(JsonData.of(LocalDate.now().atStartOfDay(ZoneOffset.UTC).toInstant().toString())) // UTC 시간으로 변환
						)
					)
					.aggregations("popular_searches", a -> a
						.terms(t -> t
							.field("search_query.keyword")
							.size(size)
						)
					),
				Void.class
			);

			List<PopularSearchDto> popularSearches = new ArrayList<>();
			List<StringTermsBucket> buckets = response.aggregations()
				.get("popular_searches")
				.sterms()
				.buckets().array();

			for (StringTermsBucket bucket : buckets) {
				popularSearches.add(new PopularSearchDto(bucket.key().stringValue(), bucket.docCount()));
			}

			return popularSearches;
		} catch (IOException e) {
			throw new RuntimeException("Error querying Elasticsearch", e);
		}
	}

	// @Override
	// public List<String> findPopularSearches(int size) {
	// 	return searchQueryRepository.findTopSearchQueries(size);
	// }
	//
	// @Override
	// public List<String> findPopularSearchesByTimeRange(int size, int days) {
	// 	return searchQueryRepository.findTopSearchQueriesByTimeRange(size, days);
	// }

	@Override
	public Page<UserSearchResponseDto> findUser(Long nowid, String keyword, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);
		Page<UserDocument> userDocumentPage = userSearchRepository.findByNicknameContaining(keyword, pageable);
		System.out.println(userDocumentPage.toString());
		List<UserSearchResponseDto> userSearchResponseDtos = userDocumentPage.getContent().stream()
			.map(this::convertToUserDto)
			.collect(Collectors.toList());

		return new PageImpl<>(userSearchResponseDtos, pageable, userDocumentPage.getTotalElements());
	}


	private AssetSearchResponseDto convertToAssetDto (Long nowid,AssetSellDocument document){
		boolean isLiked = assetLikeRepository.existsByUserIdAndAssetSellId(nowid, document.getAssetSellId());

		AssetSearchResponseDto dto = new AssetSearchResponseDto(
			document.getAssetSellId(),
			document.getAssetId(),
			document.getCreator(),
			document.getImageUrls(),
			document.getPrice(),
			document.getHits(),
			document.getCreatedAt(),
			document.getLikeCount(),
			document.getTags(),
			isLiked,
			document.getPurchaseCount()
		);
		return dto;
	}
	private UserSearchResponseDto convertToUserDto( UserDocument document) {
		return new UserSearchResponseDto(
			document.getId(),
			document.getNickname(),
			document.getProfileImage()

		);
	}

	private NftSearchResponseDto convertToNFTDto (Long nowid,NFTSellDocument document){
		boolean isLiked = nftMarketLikeRepository.existsByUserIdAndMarketId(nowid, document.getNftSellId());
		NFTMarket market = nftMarketRepository.findById(document.getNftSellId())
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
		List<BigInteger> tokenIds = List.of(market.getNftId().getTokenId());
		List<NFTInfoDto> nftInfos;
		try {
			nftInfos = blockChainService.getNFTsByIds(tokenIds);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}
		NFTInfoDto nftinfo =nftInfos.get(0);

		NftSearchResponseDto nftDto = new NftSearchResponseDto(
			document.getNftSellId(),
			document.getNftId(),
			document.getSeller(),
			document.getPrice(),
			document.getLikeCount(),
			isLiked,
			nftinfo,
			document.getTags()
		);
		return nftDto;
	}


}
