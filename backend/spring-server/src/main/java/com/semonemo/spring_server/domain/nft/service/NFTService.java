package com.semonemo.spring_server.domain.nft.service;

import com.semonemo.spring_server.global.common.CursorResult;
import com.semonemo.spring_server.domain.nft.dto.response.NFTResponseDto;

public interface NFTService {
    void marketLike(Long userId, Long marketId);

    void marketDislike(Long userId, Long marketId);

    CursorResult<NFTResponseDto> getUserOwnedNFTs(Long userId, Long cursorId, int size);
}
