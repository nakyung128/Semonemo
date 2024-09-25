package com.semonemo.spring_server.domain.nft.service;

import java.util.ArrayList;
import java.util.List;

import com.semonemo.spring_server.global.exception.CustomException;
import com.semonemo.spring_server.global.exception.ErrorCode;
import org.springframework.security.core.userdetails.UserDetails;
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
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;

import jakarta.annotation.PostConstruct;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Service
@RequiredArgsConstructor
public class NFTServiceImpl implements NFTService {

    private final WebClient webClient;
    private final ObjectMapper objectMapper;

    private final NFTRepository nftRepository;
    private final NFTMarketRepository nftMarketRepository;
    private final NFTMarketLikeRepository nftMarketLikeRepository;
    private final UserRepository userRepository;

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
        nftMarketRepository.updateCount(1, marketId);
    }

    @Transactional
    @Override
    public void marketDislike(Long userId, Long marketId) {
        NFTMarketLike like = nftMarketLikeRepository.findByUserIdAndMarketId(userId, marketId);
        nftMarketLikeRepository.delete(like);
        NFTMarket nftMarket = nftMarketRepository.findById(marketId)
                .orElseThrow(() -> new CustomException(ErrorCode.NFT_MARKET_NOT_FOUND_ERROR));
        if (nftMarket.getLikeCount() > 0) {
            nftMarketRepository.updateCount(-1, marketId);
        }
    }

    @Transactional
    @Override
    public CursorResult<NFTResponseDto> getUserOwnedNFTs(Long userId, Long cursorId, int size) {
        List<NFTs> userNFTs;
        if (cursorId == null) {
            userNFTs = nftRepository.findByUserIdTopN(userId, size + 1);
        } else {
            userNFTs = nftRepository.findByUserIdNextN(userId, cursorId, size + 1);
        }
        List<NFTResponseDto> dtos = new ArrayList<>();
        boolean hasNext = false;
        if (userNFTs.size() > size) {
            hasNext = true;
            userNFTs = userNFTs.subList(0, size);
        }

        for (NFTs userNFT : userNFTs) {
            NFTResponseDto dto = nftConvertToDto(userId, userNFT.getNftId());
            dtos.add(dto);
        }

        Long nextCursorId = hasNext ? userNFTs.get(userNFTs.size() - 1).getNftId() : null;
        return new CursorResult<>(dtos, nextCursorId, hasNext);
    }

    private NFTResponseDto nftConvertToDto(Long userId, Long nftId) {
        NFTs nft = nftRepository.findById(nftId)
            .orElseThrow(() -> new CustomException(ErrorCode.NFT_NOT_FOUND_ERROR));

        boolean isOnSale = nftMarketRepository.existsByUserIdAndMarketId(userId ,nftId);

        // 외부 API 호출
        String additionalInfo = webClient.get()
            .uri("/api/token-info/{tokenId}", nft.getTokenId())
            .retrieve()
            .bodyToMono(String.class)
            .block(); // 동기적 처리를 위해 block() 사용

        return new NFTResponseDto(
            nft.getNftId(),
            nft.getCreator().getId(),
            nft.getOwner().getId(),
            nft.getTokenId(),
            nft.getIsOpen(),
            isOnSale
        );
    }

    public Mono<NFTInfo> getNFTInfo(String tokenId) {
        return webClient.get()
            .uri(uriBuilder -> uriBuilder
                .path("/bcapi/nft")
                .queryParam("tokenIds", tokenId)
                .build())
            .retrieve()
            .bodyToMono(String.class)
            .map(this::parseResponse)
            .onErrorResume(e -> {
                return Mono.error(new CustomException(ErrorCode.NODE_SERVER_ERROR));
            });
    }

    private NFTInfo parseResponse(String jsonResponse) {
        try {
            ObjectMapper objectMapper = new ObjectMapper();
            JsonNode rootNode = objectMapper.readTree(jsonResponse);

            JsonNode dataNode = rootNode.get("data").get(0);

            return new NFTInfo(
                dataNode.get("tokenId").asText(),
                dataNode.get("creator").asText(),
                dataNode.get("currentOwner").asText(),
                dataNode.get("tokenURI").asText()
            );
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
