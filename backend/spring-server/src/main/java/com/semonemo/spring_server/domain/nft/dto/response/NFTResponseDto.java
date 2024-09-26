package com.semonemo.spring_server.domain.nft.dto.response;

import lombok.AllArgsConstructor;
import lombok.Data;

public record NFTResponseDto (
    Long nftId,
    Long creator,
    Long owner,
    Long tokenId,
    boolean isOpen,
    boolean isOnSale
) {
}
