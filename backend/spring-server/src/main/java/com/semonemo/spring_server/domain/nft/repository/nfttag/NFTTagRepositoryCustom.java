package com.semonemo.spring_server.domain.nft.repository.nfttag;

import com.semonemo.spring_server.domain.nft.entity.NFTTag;

import java.util.List;

public interface NFTTagRepositoryCustom {
    List<String> findTagNamesByNFT(Long nftId);
}
