package com.semonemo.spring_server.domain.nft.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.util.Optional;

public record NFTMarketResponseDto (
    Long marketId,
    Long nftId,
    Long seller,
    Long price,
    int likeCount,
    boolean isLiked
) {
}
