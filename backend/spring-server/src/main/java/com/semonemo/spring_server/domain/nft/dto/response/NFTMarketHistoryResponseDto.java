package com.semonemo.spring_server.domain.nft.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

import java.time.LocalDateTime;
import java.util.Optional;

public record NFTMarketHistoryResponseDto (
    Long nftId,
    Long seller,
    Long price,
    LocalDateTime time
) {
}