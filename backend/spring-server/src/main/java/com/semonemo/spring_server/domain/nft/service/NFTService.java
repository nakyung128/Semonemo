package com.semonemo.spring_server.domain.nft.service;

import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketHistoryResponseDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;

import java.util.List;

public interface NFTService {
    NFTResponseDto mintNFT(NFTServiceRequestDto nftServiceRequestDto);

    NFTMarketResponseDto sellNFT(NFTMarketServiceRequestDto nftMarketServiceRequestDto);

    CursorResult<NFTMarketResponseDto> getSellingNFTs(Long userId, Long cursorId, int size);

    CursorResult<NFTMarketResponseDto> getUserSellingNFTs(Long seller, Long userId, Long cursorId, int size);

    CursorResult<NFTMarketResponseDto> getCreatorSellingNFTs(Long creator, Long userId, Long cursorId, int size);

    NFTMarketResponseDto getSellingNFTDetails(Long userId, Long marketId);

    List<NFTMarketHistoryResponseDto> getMarketHistory(Long nftId);

    CursorResult<NFTResponseDto> getOwnedNFTs(Long userId, Long cursorId, int size);

    CursorResult<NFTResponseDto> getUserNFTs(Long userId, Long cursorId, int size);

    NFTResponseDto getNFTDetails(Long nftId);

    void marketLike(Long userId, Long marketId);

    void marketDislike(Long userId, Long marketId);

    void nftToggleOpen(Long nftId);

    void marketBuy(Long userId, Long nftId);

    boolean checkLike(Long userId, Long marketId);
}
