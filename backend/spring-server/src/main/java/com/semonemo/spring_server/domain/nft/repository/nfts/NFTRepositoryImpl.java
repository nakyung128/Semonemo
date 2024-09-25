package com.semonemo.spring_server.domain.nft.repository.nfts;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.entity.QNFTs;

public class NFTRepositoryImpl implements NFTRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTs nfts = QNFTs.nFTs;

    @Override
    public List<NFTs> findByUserIdTopN(Long userId, int size) {
        return queryFactory
            .selectFrom(nfts)
            .where(nfts.owner.id.eq(userId))
            .orderBy(nfts.nftId.desc())
            .limit(size)
            .fetch();
    }

    @Override
    public List<NFTs> findByUserIdNextN(Long userId, Long cursorId, int size) {
        return queryFactory
            .selectFrom(nfts)
            .where(
                nfts.nftId.lt(cursorId)
                .and(nfts.owner.id.eq(userId))
            )
            .orderBy(nfts.nftId.desc())
            .limit(size)
            .fetch();
    }
}
