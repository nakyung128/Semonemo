package com.semonemo.spring_server.domain.nft.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.semonemo.spring_server.domain.coin.entity.TradeLog;
import com.semonemo.spring_server.domain.coin.repository.TradeLogRepository;
import com.semonemo.spring_server.domain.user.dto.response.UserInfoResponseDTO;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.elasticsearch.service.ElasticsearchSyncService;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.domain.nft.entity.*;
import com.semonemo.spring_server.domain.nft.repository.nfttag.NFTTagRepository;
import com.semonemo.spring_server.domain.nft.repository.ntags.NTagRepository;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;


import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;

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
	private final NFTRepository nftRepository;
	private final NFTMarketRepository nftMarketRepository;
	private final NFTMarketLikeRepository nftMarketLikeRepository;
	private final UserRepository userRepository;
	private final NTagRepository nTagRepository;
	private final NFTTagRepository nftTagRepository;
    private final TradeLogRepository tradeLogRepository;
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
            .frameType(nftServiceRequestDto.getFrameType())
			.isOpen(true)
			.isOnSale(false)
			.build();

		nftRepository.save(nft);

        NFTInfoDto nftInfo;
        try {
            nftInfo = blockChainService.getNFTById(nft.getTokenId());
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

        UserInfoResponseDTO creatorInfo = new UserInfoResponseDTO(
            nft.getCreator().getId(),
            nft.getCreator().getAddress(),
            nft.getCreator().getNickname(),
            nft.getCreator().getProfileImage()
        );

        UserInfoResponseDTO ownerInfo = new UserInfoResponseDTO(
            nft.getCreator().getId(),
            nft.getCreator().getAddress(),
            nft.getCreator().getNickname(),
            nft.getCreator().getProfileImage()
        );

        return new NFTResponseDto(
            nft.getNftId(),
            creatorInfo,
            ownerInfo,
            nft.getTokenId(),
            nft.getFrameType(),
            tagNames,
            nft.getIsOpen(),
            nft.getIsOnSale(),
            nftInfo
        );
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
			throw new CustomException(ErrorCode.OWNER_NOT_MATCH);
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

        NFTInfoDto nftInfo;
        try {
            nftInfo = blockChainService.getNFTById(market.getNftId().getTokenId());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

		List<String> tagNames = nftTagRepository.findTagNamesByNFT(nft.getNftId());

        UserInfoResponseDTO sellerInfo = new UserInfoResponseDTO(
            market.getSeller().getId(),
            market.getSeller().getAddress(),
            market.getSeller().getNickname(),
            market.getSeller().getProfileImage()
        );

        syncService.syncNFTMarket(nft, market, nft.getTags());
        return new NFTMarketResponseDto(
            market.getMarketId(),
            market.getNftId().getNftId(),
            sellerInfo,
            market.getPrice(),
            market.getLikeCount(),
            false,
            nftInfo,
            tagNames
        );
	}

    @Transactional
    @Override
    public void cancelNFTMarket(Long userId, Long marketId) {
        Users user = userRepository.findById(userId)
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        NFTMarket market = nftMarketRepository.findById(marketId)
            .orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));

        // 본인 소유의 판매글이 아님
        if (!user.getId().equals(market.getSeller().getId())) {
            throw new CustomException(ErrorCode.OWNER_NOT_MATCH);
        }

        // 이미 판매완료된 항목임
        if (market.getIsSold()) {
            throw new CustomException(ErrorCode.NFT_ALREADY_ON_SALE);
        }

        NFTs nft = nftRepository.findById(market.getNftId().getNftId())
            .orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));

        nft.toggleOnSale(false);

        nftMarketRepository.delete(market);
        syncService.deleteNftData(marketId);
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
            case "dislike" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "likeCount"));
			case "oldest" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "createdAt"));
			default -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "createdAt"));
		};

		Page<NFTMarket> sellingNFTs = nftMarketRepository.findNotSold(pageable);

		List<NFTMarketResponseDto> dtos = new ArrayList<>();

		List<BigInteger> tokenIds = sellingNFTs.getContent().stream()
			.map(nft -> nft.getNftId().getTokenId())
			.toList();

		List<NFTInfoDto> allNFTInfo;
		try {
			allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
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
            case "dislike" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "likeCount"));
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
	public Page<NFTMarketResponseDto> getCreatorSellingNFTs(Long creator, Long userId, String orderBy, int page, int size) {
		Pageable pageable = null;

		pageable = switch (orderBy) {
			case "high" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "price"));
			case "low" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "price"));
			case "like" -> PageRequest.of(page, size, Sort.by(Sort.Direction.DESC, "likeCount"));
            case "dislike" -> PageRequest.of(page, size, Sort.by(Sort.Direction.ASC, "likeCount"));
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

    // 좋아요한 NFT 마켓 조회
    @Transactional
    @Override
    public Page<NFTMarketResponseDto> getLikedSellingNFTs(Long userId, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<NFTMarket> likedNFTs = nftMarketRepository.findByLiked(userId, pageable);

        List<NFTMarketResponseDto> dtos = new ArrayList<>();

        List<BigInteger> tokenIds = likedNFTs.getContent().stream()
            .map(likedNFT -> likedNFT.getNftId().getTokenId())
            .toList();

        List<NFTInfoDto> allNFTInfo;
        try {
            allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
            .collect(Collectors.toMap(
                NFTInfoDto::getTokenId,
                info -> info
            ));

        for (NFTMarket likedNFT : likedNFTs.getContent()) {
            NFTInfoDto nftInfo = nftInfoMap.get(likedNFT.getNftId().getTokenId());
            NFTMarketResponseDto dto = nftMarketConvertToDto(userId, likedNFT, nftInfo);
            dtos.add(dto);
        }

        return new PageImpl<>(dtos, pageable, likedNFTs.getTotalElements());
    }

	// 판매 NFT 상세 조회
	@Transactional
	@Override
	public NFTMarketResponseDto getSellingNFTDetails(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));

        NFTInfoDto nftInfo;
        try {
            nftInfo = blockChainService.getNFTById(market.getNftId().getTokenId());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

		return nftMarketConvertToDto(userId, market, nftInfo);
	}

	// NFT 판매 기록 (가격, 판매시점, 판매자)
	@Transactional
	@Override
	public List<NFTMarketHistoryResponseDto> getMarketHistory(Long nftId) {
		List<NFTMarket> market = nftMarketRepository.findSold(nftId);

		return market.stream()
			.map(m -> new NFTMarketHistoryResponseDto(
				nftId,
                new UserInfoResponseDTO(
                    m.getSeller().getId(),
                    m.getSeller().getAddress(),
                    m.getSeller().getNickname(),
                    m.getSeller().getProfileImage()
                ),
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

		NFTInfoDto nftInfo;
        try {
            nftInfo = blockChainService.getNFTById(nft.getTokenId());
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

		return nftConvertToDto(nft, nftInfo);
	}

	// 마켓 좋아요
	@Transactional
	@Override
	public int marketLike(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
		Users user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		NFTMarketLike like = NFTMarketLike.builder()
			.marketId(market)
			.likedUserId(user)
			.build();
		nftMarketLikeRepository.save(like);
		int afterLikedCount = market.updateLikeCount(1);
		syncService.syncNftData(marketId, "like");

        return afterLikedCount;
	}

	// 마켓 좋아요 취소
	@Transactional
	@Override
	public int marketDislike(Long userId, Long marketId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
		NFTMarketLike like = nftMarketLikeRepository.findByUserIdAndMarketId(userId, marketId);

		nftMarketLikeRepository.delete(like);

        int afterLikedCount = market.updateLikeCount(-1);
		syncService.syncNftData(marketId, "like");

        return afterLikedCount;
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
	public void marketBuy(Long userId, Long marketId, BigInteger tradeId) {
		NFTMarket market = nftMarketRepository.findById(marketId)
			.orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));

		NFTs nft = market.getNftId();

		Users user = userRepository.findById(userId)
			.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

		nft.toggleOnSale(false);
		nft.changeOwner(user);
		market.markAsSold();

        user.minusBalance(market.getPrice());
        market.getSeller().plusBalance(market.getPrice());

        TradeLog tradeLog = TradeLog.builder()
            .tradeId(tradeId)
            .fromUser(user)
            .toUser(market.getSeller())
            .amount(market.getPrice())
            .tradeType("NFT 거래")
            .build();
        tradeLogRepository.save(tradeLog);
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

    @Transactional
    @Override
    public Page<NFTResponseDto> getOwnedNFTsByType(Long userId, int type, int page, int size) {
        Pageable pageable = PageRequest.of(page, size);

        Page<NFTs> userNFTs = nftRepository.findOwnedByUserAndType(userId, type, pageable);

        List<NFTResponseDto> dtos = new ArrayList<>();

        List<BigInteger> tokenIds = userNFTs.getContent().stream()
            .map(NFTs::getTokenId)
            .toList();

        List<NFTInfoDto> allNFTInfo;
        try {
            allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
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

    @Transactional
    @Override
    public List<NFTMarketResponseDto> getHotNFTs(Long userId) {
        List<NFTMarket> hotNFTs = nftMarketRepository.findHot();

        List<NFTMarketResponseDto> dtos = new ArrayList<>();

        List<BigInteger> tokenIds = hotNFTs.stream()
            .map(nft -> nft.getNftId().getTokenId())
            .toList();

        List<NFTInfoDto> allNFTInfo;
        try {
            allNFTInfo = blockChainService.getNFTsByIds(tokenIds);
        } catch (Exception e) {
            throw new CustomException(ErrorCode.BLOCKCHAIN_ERROR);
        }

        Map<BigInteger, NFTInfoDto> nftInfoMap = allNFTInfo.stream()
            .collect(Collectors.toMap(
                NFTInfoDto::getTokenId,
                info -> info
            ));

        for (NFTMarket nft : hotNFTs) {
            NFTInfoDto nftInfo = nftInfoMap.get(nft.getNftId().getTokenId());
            NFTMarketResponseDto dto = nftMarketConvertToDto(userId, nft, nftInfo);
            dtos.add(dto);
        }

        return dtos;
    }

	// Utils
	private NFTMarketResponseDto nftMarketConvertToDto(Long userId, NFTMarket sellingNFT, NFTInfoDto nftInfo) {
		// 좋아요 여부 확인
		boolean isLiked = nftMarketLikeRepository.existsByUserIdAndMarketId(userId, sellingNFT.getMarketId());

		List<String> tagNames = nftTagRepository.findTagNamesByNFT(sellingNFT.getNftId().getNftId());

        UserInfoResponseDTO sellerInfo = new UserInfoResponseDTO(
            sellingNFT.getSeller().getId(),
            sellingNFT.getSeller().getAddress(),
            sellingNFT.getSeller().getNickname(),
            sellingNFT.getSeller().getProfileImage()
        );

		// 외부 API 호출
		return new NFTMarketResponseDto(
			sellingNFT.getMarketId(),
			sellingNFT.getNftId().getNftId(),
            sellerInfo,
			sellingNFT.getPrice(),
			sellingNFT.getLikeCount(),
			isLiked,
			nftInfo,
			tagNames
		);
	}

	private NFTResponseDto nftConvertToDto(NFTs nft, NFTInfoDto nftInfo) {
		List<String> tagNames = nftTagRepository.findTagNamesByNFT(nft.getNftId());

        UserInfoResponseDTO creatorInfo = new UserInfoResponseDTO(
            nft.getCreator().getId(),
            nft.getCreator().getAddress(),
            nft.getCreator().getNickname(),
            nft.getCreator().getProfileImage()
        );

        UserInfoResponseDTO ownerInfo = new UserInfoResponseDTO(
            nft.getOwner().getId(),
            nft.getOwner().getAddress(),
            nft.getOwner().getNickname(),
            nft.getOwner().getProfileImage()
        );

		return new NFTResponseDto(
			nft.getNftId(),
            creatorInfo,
            ownerInfo,
			nft.getTokenId(),
            nft.getFrameType(),
			tagNames,
			nft.getIsOpen(),
			nft.getIsOnSale(),
			nftInfo
		);
	}
}
