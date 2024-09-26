package com.semonemo.spring_server.domain.nft.repository.nftmarketlike;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTMarketLike;
import com.semonemo.spring_server.domain.nft.entity.QNFTMarketLike;

public class NFTMarketLikeRepositoryImpl implements NFTMarketLikeRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTMarketLikeRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTMarketLike nftMarketLike = QNFTMarketLike.nFTMarketLike;

    @Override
    public NFTMarketLike findByUserIdAndMarketId(Long userId, Long marketId) {
        return queryFactory
            .selectFrom(nftMarketLike)
            .where(nftMarketLike.likedUserId.id.eq(userId)
                .and(nftMarketLike.marketId.marketId.eq(marketId)))
            .fetchOne();
    }

    @Override
    public boolean existsByUserIdAndMarketId(Long userId, Long marketId) {
        return queryFactory
            .selectFrom(nftMarketLike)
            .where(nftMarketLike.likedUserId.id.eq(userId)
                .and(nftMarketLike.marketId.marketId.eq(marketId)))
            .fetchOne() != null;
    }
}
