package com.semonemo.spring_server.domain.nft.repository.nftmarket;

import java.util.List;
import java.util.Optional;

import com.querydsl.core.types.OrderSpecifier;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.QNFTMarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

public class NFTMarketRepositoryImpl implements NFTMarketRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTMarketRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTMarket nftMarket = QNFTMarket.nFTMarket;

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
    public Page<NFTMarket> findBySeller(Long owner, Pageable pageable) {
        List<NFTMarket> content = queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.isSold.isFalse()
                    .and(nftMarket.nftId.owner.id.eq(owner))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifier(pageable))
            .fetch();

        long total = Optional.ofNullable(queryFactory
            .select(nftMarket.count())
            .from(nftMarket)
            .where(
                nftMarket.isSold.isFalse()
                    .and(nftMarket.nftId.owner.id.eq(owner))
            )
            .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    @Override
    public Page<NFTMarket> findByCreator(Long creator, Pageable pageable) {
        List<NFTMarket> content = queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.isSold.isFalse()
                    .and(nftMarket.nftId.creator.id.eq(creator))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .orderBy(getOrderSpecifier(pageable))
            .fetch();

        long total = Optional.ofNullable(queryFactory
            .select(nftMarket.count())
            .from(nftMarket)
            .where(
                nftMarket.isSold.isFalse()
                    .and(nftMarket.nftId.creator.id.eq(creator))
            )
            .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }

    private OrderSpecifier<?> getOrderSpecifier(Pageable pageable) {
        if (!pageable.getSort().isEmpty()) {
            for (Sort.Order order : pageable.getSort()) {
                switch (order.getProperty()) {
                    case "price":
                        return order.isAscending() ? nftMarket.price.asc() : nftMarket.price.desc();
                    case "likeCount":
                        return order.isAscending() ? nftMarket.likeCount.asc() : nftMarket.likeCount.desc();
                    case "createdAt":
                        return order.isAscending() ? nftMarket.createdAt.asc() : nftMarket.createdAt.desc();
                    // 필요한 다른 정렬 기준 추가
                }
            }
        }
        return nftMarket.createdAt.desc(); // 기본 정렬
    }

    @Override
    public boolean existsOnSaleByNftId(Long nftId) {
        return queryFactory
            .selectFrom(nftMarket)
            .where(
                nftMarket.nftId.nftId.eq(nftId)
                    .and(nftMarket.isSold.isFalse())
            )
            .fetchOne() != null;
    }
}
