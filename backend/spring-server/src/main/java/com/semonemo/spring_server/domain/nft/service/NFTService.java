package com.semonemo.spring_server.domain.nft.service;

import com.semonemo.spring_server.domain.nft.dto.response.NFTMarketResponseDto;
import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.nft.dto.request.NFTServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.request.NFTMarketServiceRequestDto;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;

public interface NFTService {
    NFTResponseDto mintNFT(NFTServiceRequestDto nftServiceRequestDto);

    NFTMarketResponseDto sellNFT(NFTMarketServiceRequestDto nftMarketServiceRequestDto);

    CursorResult<NFTMarketResponseDto> getSellingNFTs(Long userId, Long cursorId, int size);

    CursorResult<NFTResponseDto> getOwnedNFTs(Long userId, Long cursorId, int size);

    CursorResult<NFTResponseDto> getUserNFTs(Long userId, Long cursorId, int size);

    void marketLike(Long userId, Long marketId);

    void marketDislike(Long userId, Long marketId);

//    CursorResult<NFTResponseDto> getSellingNFTs(Long userId, Long cursorId, int size);
}
