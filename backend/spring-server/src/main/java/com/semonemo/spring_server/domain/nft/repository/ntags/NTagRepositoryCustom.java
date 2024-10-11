package com.semonemo.spring_server.domain.nft.repository.ntags;

import com.semonemo.spring_server.domain.nft.entity.Ntags;

import java.util.List;

public interface NTagRepositoryCustom {
    Ntags findByName(String tagName);
}
