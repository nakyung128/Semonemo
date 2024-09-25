package com.semonemo.spring_server.domain.nft.dto.response;

public record NFTResponseDto (
        Long nftId,
        Long creator,
        Long owner,
        String tokenId,
        boolean isOpen,
        boolean isOnSale
) {
}
