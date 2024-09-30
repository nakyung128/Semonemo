package com.semonemo.spring_server.domain.nft.repository.nfts;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.semonemo.spring_server.domain.nft.entity.NFTs;

@Repository
public interface NFTRepository extends JpaRepository<NFTs, Long>, NFTRepositoryCustom {
}
