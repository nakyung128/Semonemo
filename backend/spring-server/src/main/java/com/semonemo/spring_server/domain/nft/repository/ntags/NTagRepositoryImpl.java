package com.semonemo.spring_server.domain.nft.repository.ntags;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.Ntags;
import com.semonemo.spring_server.domain.nft.entity.QNtags;

import java.util.List;

public class NTagRepositoryImpl implements NTagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NTagRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNtags ntags = QNtags.ntags;

    @Override
    public Ntags findByName(String name) {
        return queryFactory
            .selectFrom(ntags)
            .where(ntags.name.eq(name))
            .fetchOne();
    }
}
