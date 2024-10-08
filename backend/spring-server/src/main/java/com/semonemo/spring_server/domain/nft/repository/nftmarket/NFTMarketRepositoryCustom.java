package com.semonemo.spring_server.domain.nft.repository.nftmarket;

import com.semonemo.spring_server.domain.nft.entity.NFTMarket;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface NFTMarketRepositoryCustom {
    List<NFTMarket> findSold(Long nftId);

    Page<NFTMarket> findNotSold(Pageable pageable);

    Page<NFTMarket> findBySeller(Long owner, Pageable pageable);

    Page<NFTMarket> findByCreator(Long creator, Pageable pageable);

    Page<NFTMarket> findByLiked(Long userId, Pageable pageable);

    List<NFTMarket> findHot();

    boolean existsOnSaleByNftId(Long nftId);
}