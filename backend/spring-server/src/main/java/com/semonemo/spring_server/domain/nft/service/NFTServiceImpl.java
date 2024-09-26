package com.semonemo.spring_server.domain.nft.service;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import reactor.core.publisher.Mono;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

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
import lombok.AllArgsConstructor;
import lombok.Data;

@Service
@RequiredArgsConstructor
public class NFTServiceImpl implements NFTService {

    private static final Log log = LogFactory.getLog(NFTServiceImpl.class);
    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final NFTRepository nftRepository;
    private final NFTMarketRepository nftMarketRepository;
    private final NFTMarketLikeRepository nftMarketLikeRepository;
    private final UserRepository userRepository;

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

        return new NFTResponseDto(
            nft.getNftId(),
            nft.getCreator().getId(),
            nft.getOwner().getId(),
            nft.getTokenId(),
            nft.getIsOpen(),
            nft.getIsOnSale()
        );
    }

    @Transactional
    @Override
    public NFTMarketResponseDto sellNFT(NFTMarketServiceRequestDto nftMarketServiceRequestDto) {
        Users user = userRepository.findById(nftMarketServiceRequestDto.getSeller())
            .orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND_ERROR));

        NFTs nft = nftRepository.findById(nftMarketServiceRequestDto.getNftId())
            .orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));
        
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

        return new NFTMarketResponseDto(
            market.getMarketId(),
            market.getNftId().getNftId(),
            market.getSeller().getId(),
            market.getPrice(),
            market.getLikeCount(),
            false
        );
    }

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

            String tokenIds = sellingNFTs.stream()
                .map(nft -> nft.getNftId().getTokenId().toString())
                .collect(Collectors.joining(","));

            List<NFTInfo> result = getNFTInfo(tokenIds);
            log.info(result);

        for (NFTMarket sellingNFT : sellingNFTs) {
            NFTMarketResponseDto dto = nftMarketConvertToDto(userId, sellingNFT);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? sellingNFTs.get(sellingNFTs.size() - 1).getMarketId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

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

        for (NFTs userNFT : userNFTs) {
            NFTResponseDto dto = nftConvertToDto(userNFT);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? userNFTs.get(userNFTs.size() - 1).getNftId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

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

        for (NFTs userNFT : userNFTs) {
            NFTResponseDto dto = nftConvertToDto(userNFT);
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? userNFTs.get(userNFTs.size() - 1).getNftId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

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


    // Utils
    private NFTMarketResponseDto nftMarketConvertToDto(Long userId, NFTMarket sellingNFT) {
        // 좋아요 여부 확인
        boolean isLiked = nftMarketLikeRepository.existsByUserIdAndMarketId(userId, sellingNFT.getMarketId());

        // 외부 API 호출

        return new NFTMarketResponseDto(
            sellingNFT.getMarketId(),
            sellingNFT.getNftId().getNftId(),
            sellingNFT.getSeller().getId(),
            sellingNFT.getPrice(),
            sellingNFT.getLikeCount(),
            isLiked
        );
    }

    private NFTResponseDto nftConvertToDto(NFTs nft) {
        // 외부 API 호출
        String additionalInfo = webClient.get()
            .uri("/api/token-info/{tokenId}", nft.getTokenId())
            .retrieve()
            .bodyToMono(String.class)
            .block(); // 동기적 처리를 위해 block() 사용

        log.info(additionalInfo);

        return new NFTResponseDto(
            nft.getNftId(),
            nft.getCreator().getId(),
            nft.getOwner().getId(),
            nft.getTokenId(),
            nft.getIsOpen(),
            nft.getIsOnSale()
        );
    }

    public List<NFTInfo> getNFTInfo(String tokenId) {
        try {
            return webClient.get()
                .uri(uriBuilder -> uriBuilder
                    .path("/bcapi/nft")
                    .queryParam("tokenIds", tokenId)
                    .build())
                .retrieve()
                .bodyToMono(String.class)
                .map(this::parseResponse)
                .block(); // 여기서 동기적으로 결과를 기다림
        } catch (Exception e) {
            throw new CustomException(ErrorCode.NODE_SERVER_ERROR);
        }
    }

    private List<NFTInfo> parseResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);
            JsonNode dataArray = rootNode.get("data");

            if (dataArray == null || !dataArray.isArray()) {
                throw new CustomException(ErrorCode.JSON_PARSING_ERROR);
            }

            List<NFTInfo> nftInfoList = new ArrayList<>();

            for (JsonNode dataNode : dataArray) {
                NFTInfo nftInfo = new NFTInfo(
                    dataNode.get("tokenId").asText(),
                    dataNode.get("creator").asText(),
                    dataNode.get("currentOwner").asText(),
                    dataNode.get("tokenURI").asText()
                );
                nftInfoList.add(nftInfo);
            }

            return nftInfoList;
        } catch (Exception e) {
            // 에러 처리 로직
            throw new CustomException(ErrorCode.JSON_PARSING_ERROR);
        }
    }

    @Data
    @AllArgsConstructor
    public static class NFTInfo {
        private String tokenId;
        private String creator;
        private String currentOwner;
        private String tokenURI;
    }
}
