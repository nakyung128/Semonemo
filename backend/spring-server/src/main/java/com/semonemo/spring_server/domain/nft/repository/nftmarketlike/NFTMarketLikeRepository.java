package com.semonemo.spring_server.domain.nft.repository.nftmarketlike;

import com.semonemo.spring_server.domain.nft.entity.NFTMarketLike;
import org.springframework.data.jpa.repository.JpaRepository;

public interface NFTMarketLikeRepository extends JpaRepository<NFTMarketLike, Long>, NFTMarketLikeRepositoryCustom {
}
