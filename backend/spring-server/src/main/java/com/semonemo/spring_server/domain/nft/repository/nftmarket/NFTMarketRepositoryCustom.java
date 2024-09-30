package com.semonemo.spring_server.domain.nft.repository.nftmarket;

import com.semonemo.spring_server.domain.nft.entity.NFTMarket;

import java.util.List;

public interface NFTMarketRepositoryCustom {
    List<NFTMarket> findSellingTopN(int size);

    List<NFTMarket> findSellingNextN(Long cursorId, int size);

    List<NFTMarket> findSold(Long nftId);

    List<NFTMarket> findUserSellingTopN(Long userId, int size);

    List<NFTMarket> findUserSellingNextN(Long userId, Long cursorId, int size);

    List<NFTMarket> findCreatorSellingTopN(Long userId, int size);

    List<NFTMarket> findCreatorSellingNextN(Long userId, Long cursorId, int size);
}