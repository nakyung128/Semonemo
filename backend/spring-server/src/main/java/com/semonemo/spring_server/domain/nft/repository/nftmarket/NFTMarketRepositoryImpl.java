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
    public List<NFTMarket> findSellingTopN(int size) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(nftMarket.isSold.isFalse())
            .orderBy(nftMarket.marketId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTMarket> findSellingNextN(Long cursorId, int size) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.marketId.lt(cursorId)
                .and(nftMarket.isSold.isFalse())
            )
            .orderBy(nftMarket.marketId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTMarket> findSold(Long nftId) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.isSold.isTrue()
                .and(nftMarket.marketId.eq(nftId))
            )
            .orderBy(nftMarket.marketId.desc())
            .fetch();
    }

    @Override
    public List<NFTMarket> findUserSellingTopN(Long userId, int size) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.isSold.isFalse()
                .and(nftMarket.seller.id.eq(userId))
            )
            .orderBy(nftMarket.marketId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTMarket> findUserSellingNextN(Long userId, Long cursorId, int size) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.marketId.lt(cursorId)
                .and(nftMarket.isSold.isFalse())
                .and(nftMarket.seller.id.eq(userId))
            )
            .orderBy(nftMarket.marketId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTMarket> findCreatorSellingTopN(Long userId, int size) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.isSold.isFalse()
                    .and(nftMarket.nftId.creator.id.eq(userId))
            )
            .orderBy(nftMarket.marketId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTMarket> findCreatorSellingNextN(Long userId, Long cursorId, int size) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.marketId.lt(cursorId)
                    .and(nftMarket.isSold.isFalse())
                    .and(nftMarket.nftId.creator.id.eq(userId))
            )
            .orderBy(nftMarket.marketId.desc())
            .limit(size)
            .fetch();
    }
}
