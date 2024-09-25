package com.semonemo.spring_server.domain.nft.repository.nftmarket;

public interface NFTMarketRepositoryCustom {
    void updateCount(int count, Long marketId);

    boolean existsByUserIdAndMarketId(Long userId, Long nftId);
}