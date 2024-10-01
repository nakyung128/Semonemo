package com.semonemo.spring_server.domain.nft.service;

import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;
import org.springframework.data.domain.Page;

import java.math.BigInteger;
import java.util.List;

public interface NFTService {
    NFTResponseDto mintNFT(NFTServiceRequestDto nftServiceRequestDto);

    NFTMarketResponseDto sellNFT(NFTMarketServiceRequestDto nftMarketServiceRequestDto);

    Page<NFTMarketResponseDto> getSellingNFTs(Long userId, String orderBy, int page, int size);

    Page<NFTMarketResponseDto> getUserSellingNFTs(Long seller, Long userId, String orderBy, int page, int size);

    Page<NFTMarketResponseDto> getCreatorSellingNFTs(Long creator, Long userId, String orderBy, int page, int size);

    NFTMarketResponseDto getSellingNFTDetails(Long userId, Long marketId);

    List<NFTMarketHistoryResponseDto> getMarketHistory(Long nftId);

    Page<NFTResponseDto> getOwnedNFTs(Long userId, int page, int size);

    Page<NFTResponseDto> getUserNFTs(Long userId, int page, int size);

    NFTResponseDto getNFTDetails(Long nftId);

    void marketLike(Long userId, Long marketId);

    void marketDislike(Long userId, Long marketId);

    void nftToggleOpen(Long nftId);

    void marketBuy(Long userId, Long marketId);

    boolean checkTokenId(BigInteger tokenId);

    boolean checkMarket(Long nftId);

    boolean checkLike(Long userId, Long marketId);

    boolean checkOnSale(Long marketId);
}
