package com.semonemo.spring_server.domain.nft.repository.nftmarket;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.QNFTMarket;

public class NFTMarketRepositoryImpl implements NFTMarketRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTMarketRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTMarket nftMarket = QNFTMarket.nFTMarket;

    @Override
    public void updateCount(int count, Long marketId) {
        queryFactory
            .update(nftMarket)
            .where(nftMarket.marketId.eq(marketId))
            .set(nftMarket.likeCount, nftMarket.likeCount.add(count))
            .execute();
    }

    @Override
    public boolean existsByUserIdAndMarketId(Long userId, Long nftId) {
        NFTMarket fetchOne = queryFactory
            .selectFrom(nftMarket)
            .where(nftMarket.nftId.nftId.eq(nftId)
                .and(nftMarket.seller.id.eq(userId))
                .and(nftMarket.isSold.isTrue()))
            .fetchFirst();
        return fetchOne != null;
    }
}
