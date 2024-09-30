package com.semonemo.spring_server.domain.nft.repository.nfts;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.QNFTs;
import com.semonemo.spring_server.domain.nft.entity.QNFTMarket;

public class NFTRepositoryImpl implements NFTRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTs nfts = QNFTs.nFTs;
    QNFTMarket nftMarket = QNFTMarket.nFTMarket;

    @Override
    public List<NFTs> findOwnedByUserIdTopN(Long userId, int size) {
        return queryFactory
            .selectFrom(nfts)
            .where(nfts.owner.id.eq(userId)
                .and(nfts.isOnSale.isFalse()))
            .orderBy(nfts.nftId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTs> findOwnedByUserIdNextN(Long userId, Long cursorId, int size) {
        return queryFactory
            .selectFrom(nfts)
            .where(
                nfts.nftId.lt(cursorId)
                .and(nfts.owner.id.eq(userId)
                .and(nfts.isOnSale.isFalse()))
            )
            .orderBy(nfts.nftId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTs> findPublicByUserIdTopN(Long userId, int size) {
        return queryFactory
                .selectFrom(nfts)
                .where(nfts.owner.id.eq(userId)
                    .and(nfts.isOnSale.isFalse())
                    .and(nfts.isOpen.isTrue()))
                .orderBy(nfts.nftId.desc())
                .limit(size)
                .fetch();
    }

    @Override
    public List<NFTs> findPublicByUserIdNextN(Long userId, Long cursorId, int size) {
        return queryFactory
                .selectFrom(nfts)
                .where(
                    nfts.nftId.lt(cursorId)
                    .and(nfts.owner.id.eq(userId)
                    .and(nfts.isOnSale.isFalse())
                    .and(nfts.isOpen.isTrue()))
                )
                .orderBy(nfts.nftId.desc())
                .limit(size)
                .fetch();
    }
}
