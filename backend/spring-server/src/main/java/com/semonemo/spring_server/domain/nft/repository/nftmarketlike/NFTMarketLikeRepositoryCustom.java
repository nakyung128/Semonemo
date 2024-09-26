package com.semonemo.spring_server.domain.nft.repository.nftmarketlike;

import java.util.List;

import com.semonemo.spring_server.domain.nft.entity.NFTMarketLike;

public interface NFTMarketLikeRepositoryCustom {
    NFTMarketLike findByUserIdAndMarketId(Long userId, Long marketId);

    boolean existsByUserIdAndMarketId(Long userId, Long marketId);
}
