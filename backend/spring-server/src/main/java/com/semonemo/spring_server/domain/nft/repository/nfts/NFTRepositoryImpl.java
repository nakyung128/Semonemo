package com.semonemo.spring_server.domain.nft.repository.nfts;

import java.math.BigInteger;
import java.util.List;
import java.util.Optional;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import com.semonemo.spring_server.domain.nft.entity.QNFTs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

public class NFTRepositoryImpl implements NFTRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTs nfts = QNFTs.nFTs;

    @Override
    public Page<NFTs> findOwnedByUser(Long userId, Pageable pageable) {
        List<NFTs> content = queryFactory
            .selectFrom(nfts)
            .where(
                nfts.owner.id.eq(userId)
                    .and(nfts.isOnSale.isFalse())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(queryFactory
            .select(nfts.count())
            .from(nfts)
            .where(
                nfts.owner.id.eq(userId)
                    .and(nfts.isOnSale.isFalse())
            )
            .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<NFTs> findPublicByUser(Long userId, Pageable pageable) {
        List<NFTs> content = queryFactory
            .selectFrom(nfts)
            .where(
                nfts.owner.id.eq(userId)
                    .and(nfts.isOnSale.isFalse())
                    .and(nfts.isOpen.isTrue())
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(queryFactory
            .select(nfts.count())
            .from(nfts)
            .where(
                nfts.owner.id.eq(userId)
                    .and(nfts.isOnSale.isFalse())
                    .and(nfts.isOpen.isTrue())
            )
            .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public boolean existsByTokenId(BigInteger tokenId) {
        return queryFactory
            .selectFrom(nfts)
            .where(nfts.tokenId.eq(tokenId))
            .fetchOne() != null;
    }
}
