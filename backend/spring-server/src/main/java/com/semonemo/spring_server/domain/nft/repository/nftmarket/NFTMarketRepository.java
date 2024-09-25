package com.semonemo.spring_server.domain.nft.repository.nftmarket;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.semonemo.spring_server.domain.nft.entity.NFTMarket;

@Repository
public interface NFTMarketRepository extends JpaRepository<NFTMarket, Long>, NFTMarketRepositoryCustom {
}
