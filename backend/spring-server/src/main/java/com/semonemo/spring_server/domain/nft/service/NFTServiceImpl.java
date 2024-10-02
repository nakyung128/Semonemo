package com.semonemo.spring_server.domain.nft.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.semonemo.spring_server.domain.asset.dto.AssetSellResponseDto;
import com.semonemo.spring_server.domain.asset.model.AssetSell;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfo;
import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.domain.nft.entity.*;
import com.semonemo.spring_server.domain.nft.repository.nfttag.NFTTagRepository;
import com.semonemo.spring_server.domain.nft.repository.ntags.NTagRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;

import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.user.repository.UserRepository;
import com.semonemo.spring_server.domain.nft.repository.nfts.NFTRepository;
import com.semonemo.spring_server.domain.nft.repository.nftmarket.NFTMarketRepository;
import com.semonemo.spring_server.domain.nft.repository.nftmarketlike.NFTMarketLikeRepository;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class NFTServiceImpl implements NFTService {

	private static final Log log = LogFactory.getLog(NFTServiceImpl.class);

	private final NFTRepository nftRepository;
	private final NFTMarketRepository nftMarketRepository;
	private final NFTMarketLikeRepository nftMarketLikeRepository;
	private final UserRepository userRepository;
	private final NTagRepository nTagRepository;
	private final NFTTagRepository nftTagRepository;
	private final ElasticsearchSyncService syncService;
	private final BlockChainService blockChainService;

	@Transactional
	@Override
	public NFTResponseDto mintNFT(NFTServiceRequestDto nftServiceRequestDto) {
		Users user = userRepository.findById(nftServiceRequestDto.getUserId())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		NFTs nft = NFTs.builder()
			.creator(user)
			.owner(user)
			.tokenId(nftServiceRequestDto.getTokenId())
			.isOpen(true)
			.isOnSale(false)
			.build();

		nftRepository.save(nft);

		List<BigInteger> tokenIds = List.of(nft.getTokenId());

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		for (String tagName : nftServiceRequestDto.getTags()) {
			// 태그가 존재하는지 확인하고, 없으면 생성
			Ntags tag = nTagRepository.findByName(tagName);
			if (tag == null) {
				Ntags newTag = Ntags.builder()
					.name(tagName)
					.build();

				nTagRepository.save(newTag);

				NFTTag nftTag = NFTTag.builder()
					.nftId(nft)
					.nTagId(newTag)
					.build();

				nftTagRepository.save(nftTag);
			} else {
				NFTTag nftTag = NFTTag.builder()
					.nftId(nft)
					.nTagId(tag)
					.build();

				nftTagRepository.save(nftTag);
			}
		}

		List<String> tagNames = nftTagRepository.findTagNamesByNFT(nft.getNftId());

		if (!allNFTInfo.isEmpty()) {
			return new NFTResponseDto(
				nft.getNftId(),
				nft.getCreator().getId(),
				nft.getOwner().getId(),
				nft.getTokenId(),
				tagNames,
				nft.getIsOpen(),
				nft.getIsOnSale(),
				allNFTInfo.get(0)
			);
		} else {
			throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
		}
	}

	@Transactional
	@Override
	public NFTMarketResponseDto sellNFT(NFTMarketServiceRequestDto nftMarketServiceRequestDto) {
		Users user = userRepository.findById(nftMarketServiceRequestDto.getSeller())
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		NFTs nft = nftRepository.findById(nftMarketServiceRequestDto.getNftId())
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));

		// 본인 소유의 NFT만 판매 가능
		if (!user.getId().equals(nft.getOwner().getId())) {
			throw new CustomException(ErrorCode.NFT_ALREADY_ON_SALE);
		}

		// 이미 판매중이면 판매등록 안됨
		if (nft.getIsOnSale()) {
			throw new CustomException(ErrorCode.NFT_ALREADY_ON_SALE);
		}

		NFTMarket market = NFTMarket.builder()
			.nftId(nft)
			.seller(user)
			.price(nftMarketServiceRequestDto.getPrice())
			.likeCount(0)
			.isSold(false)
			.build();

		nft.toggleOnSale(true);
		nftMarketRepository.save(market);

		List<BigInteger> tokenIds = List.of(market.getNftId().getTokenId());

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		List<String> tagNames = nftTagRepository.findTagNamesByNFT(nft.getNftId());

		if (!allNFTInfo.isEmpty()) {
			syncService.syncNFTMarket(nft, market, nft.getTags());
			return new NFTMarketResponseDto(
				market.getMarketId(),
				market.getNftId().getNftId(),
				market.getSeller().getId(),
				market.getPrice(),
				market.getLikeCount(),
				false,
				allNFTInfo.get(0),
				tagNames
			);
		} else {
			throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
		}
	}

	// 마켓에 판매중인 모든 NFT 조회
	@Transactional
	@Override
	public Page<NFTMarketResponseDto> getSellingNFTs(Long userId, String orderBy, int page, int size) {
		Pageable pageable = null;

		pageable = switch (orderBy) {
			case "high" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "price"));
			case "low" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
			case "like" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
			case "oldest" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
			default -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		};

		Page<NFTMarket> sellingNFTs = nftMarketRepository.findAll(pageable);

		List<NFTMarketResponseDto> dtos = new ArrayList<>();

		List<BigInteger> tokenIds = sellingNFTs.getContent().stream()
			.map(nft -> nft.getNftId().getTokenId())
			.toList();

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
			.collect(Collectors.toMap(
				NFTInfoDto::getTokenId,
				info -> info
			));

		for (NFTMarket sellingNFT : sellingNFTs.getContent()) {
			NFTInfoDto nftInfo = nftInfoMap.get(sellingNFT.getNftId().getTokenId());
			NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT, nftInfo);
			dtos.add(dto);
		}

		return new PageImpl<>(dtos, pageable, sellingNFTs.getTotalElements());
	}

	// 마켓에 판매중인 특정 유저 NFT 조회
	@Transactional
	@Override
	public Page<NFTMarketResponseDto> getUserSellingNFTs(Long seller, Long userId, String orderBy, int page, int size) {
		Pageable pageable = null;

		pageable = switch (orderBy) {
			case "high" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "price"));
			case "low" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
			case "like" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
			case "oldest" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
			default -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		};

		Page<NFTMarket> sellingNFTs = nftMarketRepository.findBySeller(seller, pageable);

		List<NFTMarketResponseDto> dtos = new ArrayList<>();

		List<BigInteger> tokenIds = sellingNFTs.getContent().stream()
			.map(nft -> nft.getNftId().getTokenId())
			.toList();

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
			.collect(Collectors.toMap(
				NFTInfoDto::getTokenId,
				info -> info
			));

		for (NFTMarket sellingNFT : sellingNFTs.getContent()) {
			NFTInfoDto nftInfo = nftInfoMap.get(sellingNFT.getNftId().getTokenId());
			NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT, nftInfo);
			dtos.add(dto);
		}

		return new PageImpl<>(dtos, pageable, sellingNFTs.getTotalElements());
	}

	// 마켓에 판매중인 특정 제작자 NFT 조회
	@Transactional
	@Override
	public Page<NFTMarketResponseDto> getCreatorSellingNFTs(Long creator, Long userId, String orderBy, int page,
		int size) {
		Pageable pageable = null;

		pageable = switch (orderBy) {
			case "high" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "price"));
			case "low" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
			case "like" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
			case "oldest" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
			default -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		};

		Page<NFTMarket> sellingNFTs = nftMarketRepository.findByCreator(creator, pageable);

		List<NFTMarketResponseDto> dtos = new ArrayList<>();

		List<BigInteger> tokenIds = sellingNFTs.getContent().stream()
			.map(nft -> nft.getNftId().getTokenId())
			.toList();

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
			.collect(Collectors.toMap(
				NFTInfoDto::getTokenId,
				info -> info
			));

		for (NFTMarket sellingNFT : sellingNFTs.getContent()) {
			NFTInfoDto nftInfo = nftInfoMap.get(sellingNFT.getNftId().getTokenId());
			NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT, nftInfo);
			dtos.add(dto);
		}

		return new PageImpl<>(dtos, pageable, sellingNFTs.getTotalElements());
	}

	// 판매 NFT 상세 조회
	@Transactional
	@Override
	public NFTMarketResponseDto getSellingNFTDetails(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));

		List<BigInteger> tokenIds = List.of(market.getNftId().getTokenId());

		List<NFTInfoDto> nftInfo;

		try {
			nftInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(nftInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		return nftMarketConvertToDto(userId, market, nftInfo.get(0));
	}

	// NFT 판매 기록 (가격, 판매시점, 판매자)
	@Transactional
	@Override
	public List<NFTMarketHistoryResponseDto> getMarketHistory(Long nftId) {
		List<NFTMarket> market = nftMarketRepository.findSold(nftId);

		return market.stream()
			.map(m -> new NFTMarketHistoryResponseDto(
				nftId,
				m.getSeller().getId(),
				m.getPrice(),
				m.getSoldAt()
			))
			.collect(Collectors.toList());
	}

	// 내가 보유중인 NFT 조회
	@Transactional
	@Override
	public Page<NFTResponseDto> getOwnedNFTs(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<NFTs> userNFTs = nftRepository.findOwnedByUser(userId, pageable);

		List<NFTResponseDto> dtos = new ArrayList<>();

		List<BigInteger> tokenIds = userNFTs.getContent().stream()
			.map(NFTs::getTokenId)
			.toList();

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
			.collect(Collectors.toMap(
				NFTInfoDto::getTokenId,
				info -> info
			));

		for (NFTs userNFT : userNFTs.getContent()) {
			NFTInfoDto nftInfo = nftInfoMap.get(userNFT.getTokenId());
			NFTResponseDto dto = nftConvertToDto(userNFT, nftInfo);
			dtos.add(dto);
		}

		return new PageImpl<>(dtos, pageable, userNFTs.getTotalElements());
	}

	// 타 유저가 보유중인 NFT 조회
	@Transactional
	@Override
	public Page<NFTResponseDto> getUserNFTs(Long userId, int page, int size) {
		Pageable pageable = PageRequest.of(page, size);

		Page<NFTs> userNFTs = nftRepository.findPublicByUser(userId, pageable);

		List<NFTResponseDto> dtos = new ArrayList<>();

		List<BigInteger> tokenIds = userNFTs.getContent().stream()
			.map(NFTs::getTokenId)
			.toList();

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(allNFTInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
			.collect(Collectors.toMap(
				NFTInfoDto::getTokenId,
				info -> info
			));

		for (NFTs userNFT : userNFTs.getContent()) {
			NFTInfoDto nftInfo = nftInfoMap.get(userNFT.getTokenId());
			NFTResponseDto dto = nftConvertToDto(userNFT, nftInfo);
			dtos.add(dto);
		}

		return new PageImpl<>(dtos, pageable, userNFTs.getTotalElements());
	}

	// 유저화면에서 판매중이지 않은 NFT 조회
	@Transactional
	@Override
	public NFTResponseDto getNFTDetails(Long nftId) {
		NFTs nft = nftRepository.findById(nftId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));

		List<BigInteger> tokenIds = List.of(nft.getTokenId());

		List<NFTInfoDto> nftInfo;
		try {
			nftInfo = blockChainService.getNFTsByIds(tokenIds);
			log.info(nftInfo);
		} catch (Exception e) {
			throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
		}

		return nftConvertToDto(nft, nftInfo.get(0));
	}

	// 마켓 좋아요
	@Transactional
	@Override
	public void marketLike(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
		Users user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		NFTMarketLike like = NFTMarketLike.builder()
			.marketId(market)
			.likedUserId(user)
			.build();
		nftMarketLikeRepository.save(like);
		market.updateLikeCount(1);
		syncService.syncNftData(marketId, "like");
	}

	// 마켓 좋아요 취소
	@Transactional
	@Override
	public void marketDislike(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
		NFTMarketLike like = nftMarketLikeRepository.findByUserIdAndMarketId(userId, marketId);

		nftMarketLikeRepository.delete(like);

		if (market.getLikeCount() > 0) {
			market.updateLikeCount(-1);
		}
		syncService.syncNftData(marketId, "like");
	}

	// 공개, 비공개 전환
	@Transactional
	@Override
	public void nftToggleOpen(Long nftId) {
		NFTs nft = nftRepository.findById(nftId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));

		nft.toggleOpen();
	}

	@Transactional
	@Override
	public void marketBuy(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));

		NFTs nft = market.getNftId();

		Users user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		nft.toggleOnSale(false);
		nft.changeOwner(user);
		market.markAsSold();
	}

	@Transactional
	@Override
	public boolean checkTokenId(BigInteger tokenId) {
		return nftRepository.existsByTokenId(tokenId);
	}

	@Transactional
	@Override
	public boolean checkMarket(Long nftId) {
		return nftMarketRepository.existsOnSaleByNftId(nftId);
	}

	@Transactional
	@Override
	public boolean checkLike(Long userId, Long marketId) {
		return nftMarketLikeRepository.existsByUserIdAndMarketId(userId, marketId);
	}

	@Transactional
	@Override
	public boolean checkOnSale(Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));

		return market.getIsSold();
	}

	// Utils
	private NFTMarketResponseDto nftMarketConvertToDto(Long userId, NFTMarket sellingNFT, NFTInfoDto nftInfo) {
		// 좋아요 여부 확인
		boolean isLiked = nftMarketLikeRepository.existsByUserIdAndMarketId(userId, sellingNFT.getMarketId());

		List<String> tagNames = nftTagRepository.findTagNamesByNFT(sellingNFT.getNftId().getNftId());

		// 외부 API 호출
		return new NFTMarketResponseDto(
			sellingNFT.getMarketId(),
			sellingNFT.getNftId().getNftId(),
			sellingNFT.getSeller().getId(),
			sellingNFT.getPrice(),
			sellingNFT.getLikeCount(),
			isLiked,
			nftInfo,
			tagNames
		);
	}

	private NFTResponseDto nftConvertToDto(NFTs nft, NFTInfoDto nftInfo) {
		List<String> tagNames = nftTagRepository.findTagNamesByNFT(nft.getNftId());

		return new NFTResponseDto(
			nft.getNftId(),
			nft.getCreator().getId(),
			nft.getOwner().getId(),
			nft.getTokenId(),
			tagNames,
			nft.getIsOpen(),
			nft.getIsOnSale(),
			nftInfo
		);
	}
}
