package com.semonemo.spring_server.domain.nft.repository.nfts;

import java.util.List;

import com.semonemo.spring_server.domain.nft.entity.NFTs;

public interface NFTRepositoryCustom {
    List<NFTs> findByUserIdTopN(Long userId, int size);

    List<NFTs> findByUserIdNextN(Long userId, Long cursorId, int size);
}