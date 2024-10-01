package com.semonemo.spring_server.domain.nft.repository.nfts;

import java.math.BigInteger;
import java.util.List;

import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import com.semonemo.spring_server.domain.nft.entity.NFTs;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface NFTRepositoryCustom {
    Page<NFTs> findOwnedByUser(Long userId, Pageable pageable);

    Page<NFTs> findPublicByUser(Long userId, Pageable pageable);

    boolean existsByTokenId(BigInteger tokenId);
}
