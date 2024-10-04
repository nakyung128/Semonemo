package com.semonemo.spring_server.domain.coin.repository;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.coin.entity.TradeLog;
import com.semonemo.spring_server.domain.coin.entity.QTradeLog;
import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.util.List;
import java.util.Optional;

public class TradeLogRepositoryImpl implements TradeLogRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public TradeLogRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QTradeLog tradeLog = QTradeLog.tradeLog;

    @Override
    public Page<TradeLog> findByUser(Long userId, Pageable pageable) {
        List<TradeLog> content = queryFactory
            .selectFrom(tradeLog)
            .where(
                tradeLog.fromUser.id.eq(userId)
                    .or(tradeLog.toUser.id.eq(userId))
            )
            .offset(pageable.getOffset())
            .limit(pageable.getPageSize())
            .fetch();

        long total = Optional.ofNullable(queryFactory
            .select(tradeLog.count())
            .from(tradeLog)
            .where(
                tradeLog.fromUser.id.eq(userId)
                    .or(tradeLog.toUser.id.eq(userId))
            )
            .fetchOne()).orElse(0L);

        return new PageImpl<>(content, pageable, total);
    }
}
