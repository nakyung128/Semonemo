package com.semonemo.spring_server.domain.nft.repository.nfttag;

import com.semonemo.spring_server.domain.nft.entity.NFTTag;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface NFTTagRepository extends JpaRepository<NFTTag, Long>, NFTTagRepositoryCustom {
}
