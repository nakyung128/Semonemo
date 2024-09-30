package com.semonemo.spring_server.domain.nft.repository.nfts;

import java.util.List;

import com.semonemo.spring_server.domain.nft.entity.NFTs;

public interface NFTRepositoryCustom {
    List<NFTs> findOwnedByUserIdTopN(Long userId, int size);

    List<NFTs> findOwnedByUserIdNextN(Long userId, Long cursorId, int size);

    List<NFTs> findPublicByUserIdTopN(Long userId, int size);

    List<NFTs> findPublicByUserIdNextN(Long userId, Long cursorId, int size);
}
