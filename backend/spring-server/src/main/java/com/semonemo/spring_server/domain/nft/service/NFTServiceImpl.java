package com.semonemo.spring_server.domain.nft.service;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import com.semonemo.spring_server.domain.blockchain.dto.NFTInfo;
import com.semonemo.spring_server.domain.blockchain.dto.NFTInfoDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;

import com.semonemo.spring_server.domain.blockchain.service.BlockChainService;

import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.user.entity.Users;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.NFTMarketLike;
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

        if (!allNFTInfo.isEmpty()) {
            return new NFTResponseDto(
                nft.getNftId(),
                nft.getCreator().getId(),
                nft.getOwner().getId(),
                nft.getTokenId(),
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

        if (!allNFTInfo.isEmpty()) {
            return new NFTMarketResponseDto(
                market.getMarketId(),
                market.getNftId().getNftId(),
                market.getSeller().getId(),
                market.getPrice(),
                market.getLikeCount(),
                false,
                allNFTInfo.get(0)
            );
        } else {
            throw new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR);
        }
    }

    // 마켓에 판매중인 모든 NFT 조회
    @Transactional
    @Override
    public CursorResult<NFTMarketResponseDto> getSellingNFTs(Long userId, Long cursorId, int size) {
        List<NFTMarket> sellingNFTs;
        if (cursorId == null) {
            sellingNFTs = nftMarketRepository.findSellingTopN(size + 1);
        } else {
            sellingNFTs = nftMarketRepository.findSellingNextN(cursorId, size + 1);
        }
        List<NFTMarketResponseDto> dtos = new ArrayList<>();
        boolean hasNext = false;
        if (sellingNFTs.size() > size) {
            hasNext = true;
            sellingNFTs = sellingNFTs.subList(0, size);
        }

        List<BigInteger> tokenIds = sellingNFTs.stream()
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

        for (NFTMarket sellingNFT : sellingNFTs) {
            NFTInfoDto nftInfo = nftInfoMap.get(sellingNFT.getNftId().getTokenId());
            NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT, nftInfo);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? sellingNFTs.get(sellingNFTs.size() - 1).getMarketId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

    // 마켓에 판매중인 특정 유저 NFT 조회
    @Transactional
    @Override
    public CursorResult<NFTMarketResponseDto> getUserSellingNFTs(Long seller, Long userId, Long cursorId, int size) {
        List<NFTMarket> sellingNFTs;
        if (cursorId == null) {
            sellingNFTs = nftMarketRepository.findUserSellingTopN(seller, size + 1);
        } else {
            sellingNFTs = nftMarketRepository.findUserSellingNextN(seller, cursorId, size + 1);
        }
        List<NFTMarketResponseDto> dtos = new ArrayList<>();
        boolean hasNext = false;
        if (sellingNFTs.size() > size) {
            hasNext = true;
            sellingNFTs = sellingNFTs.subList(0, size);
        }

        List<BigInteger> tokenIds = sellingNFTs.stream()
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

        for (NFTMarket sellingNFT : sellingNFTs) {
            NFTInfoDto nftInfo = nftInfoMap.get(sellingNFT.getNftId().getTokenId());
            NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT, nftInfo);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? sellingNFTs.get(sellingNFTs.size() - 1).getMarketId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

    // 마켓에 판매중인 특정 제작자 NFT 조회
    @Transactional
    @Override
    public CursorResult<NFTMarketResponseDto> getCreatorSellingNFTs(Long creator, Long userId, Long cursorId, int size) {
        List<NFTMarket> sellingNFTs;
        if (cursorId == null) {
            sellingNFTs = nftMarketRepository.findCreatorSellingTopN(creator, size + 1);
        } else {
            sellingNFTs = nftMarketRepository.findCreatorSellingNextN(creator, cursorId, size + 1);
        }
        List<NFTMarketResponseDto> dtos = new ArrayList<>();
        boolean hasNext = false;
        if (sellingNFTs.size() > size) {
            hasNext = true;
            sellingNFTs = sellingNFTs.subList(0, size);
        }

        List<BigInteger> tokenIds = sellingNFTs.stream()
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

        for (NFTMarket sellingNFT : sellingNFTs) {
            NFTInfoDto nftInfo = nftInfoMap.get(sellingNFT.getNftId().getTokenId());
            NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT, nftInfo);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? sellingNFTs.get(sellingNFTs.size() - 1).getMarketId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
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
    public CursorResult<NFTResponseDto> getOwnedNFTs(Long userId, Long cursorId, int size) {
        List<NFTs> userNFTs;
        if (cursorId == null) {
            userNFTs = nftRepository.findOwnedByUserIdTopN(userId, size + 1);
        } else {
            userNFTs = nftRepository.findOwnedByUserIdNextN(userId, cursorId, size + 1);
        }
        List<NFTResponseDto> dtos = new ArrayList<>();
        boolean hasNext = false;
        if (userNFTs.size() > size) {
            hasNext = true;
            userNFTs = userNFTs.subList(0, size);
        }

        List<BigInteger> tokenIds = userNFTs.stream()
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

        for (NFTs userNFT : userNFTs) {
            NFTInfoDto nftInfo = nftInfoMap.get(userNFT.getTokenId());
            NFTResponseDto dto = nftConvertToDto(userNFT, nftInfo);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? userNFTs.get(userNFTs.size() - 1).getNftId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

    // 타 유저가 보유중인 NFT 조회
    @Transactional
    @Override
    public CursorResult<NFTResponseDto> getUserNFTs(Long userId, Long cursorId, int size) {
        List<NFTs> userNFTs;
        if (cursorId == null) {
            userNFTs = nftRepository.findPublicByUserIdTopN(userId, size + 1);
        } else {
            userNFTs = nftRepository.findPublicByUserIdNextN(userId, cursorId, size + 1);
        }
        List<NFTResponseDto> dtos = new ArrayList<>();
        boolean hasNext = false;
        if (userNFTs.size() > size) {
            hasNext = true;
            userNFTs = userNFTs.subList(0, size);
        }

        List<BigInteger> tokenIds = userNFTs.stream()
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

        for (NFTs userNFT : userNFTs) {
            NFTInfoDto nftInfo = nftInfoMap.get(userNFT.getTokenId());
            NFTResponseDto dto = nftConvertToDto(userNFT, nftInfo);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? userNFTs.get(userNFTs.size() - 1).getNftId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
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
    public boolean checkLike(Long userId, Long marketId) {
        return nftMarketLikeRepository.existsByUserIdAndMarketId(userId, marketId);
    }

    // Utils
    private NFTMarketResponseDto nftMarketConvertToDto(Long userId, NFTMarket sellingNFT, NFTInfoDto nftInfo) {
        // 좋아요 여부 확인
        boolean isLiked = nftMarketLikeRepository.existsByUserIdAndMarketId(userId, sellingNFT.getMarketId());

        // 외부 API 호출
        return new NFTMarketResponseDto(
            sellingNFT.getMarketId(),
            sellingNFT.getNftId().getNftId(),
            sellingNFT.getSeller().getId(),
            sellingNFT.getPrice(),
            sellingNFT.getLikeCount(),
            isLiked,
            nftInfo
        );
    }

    private NFTResponseDto nftConvertToDto(NFTs nft, NFTInfoDto nftInfo) {
        // 외부 API 호출
        return new NFTResponseDto(
            nft.getNftId(),
            nft.getCreator().getId(),
            nft.getOwner().getId(),
            nft.getTokenId(),
            nft.getIsOpen(),
            nft.getIsOnSale(),
            nftInfo
        );
    }
}
