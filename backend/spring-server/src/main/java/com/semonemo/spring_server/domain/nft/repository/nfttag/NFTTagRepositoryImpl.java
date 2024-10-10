package com.semonemo.spring_server.domain.nft.repository.nfttag;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.nft.entity.NFTTag;
import com.semonemo.spring_server.domain.nft.entity.QNFTTag;

import java.util.List;
import java.util.stream.Collectors;

public class NFTTagRepositoryImpl implements NFTTagRepositoryCustom {
    private final JPAQueryFactory queryFactory;

    public NFTTagRepositoryImpl(JPAQueryFactory queryFactory) {
        this.queryFactory = queryFactory;
    }

    QNFTTag nftTag = QNFTTag.nFTTag;

    @Override
    public List<String> findTagNamesByNFT(Long nftId) {
        List<NFTTag> nftTags =  queryFactory
            .selectFrom(nftTag)
            .where(nftTag.nftId.nftId.eq(nftId))
            .fetch();

        return nftTags.stream()
            .map(tag -> tag.getNTagId().getName())
            .collect(Collectors.toList());
    }
}
