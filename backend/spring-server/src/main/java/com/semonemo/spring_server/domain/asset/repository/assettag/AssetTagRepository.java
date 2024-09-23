package com.semonemo.spring_server.domain.asset.repository.assettag;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.asset.model.AssetTag;

public interface AssetTagRepository  extends JpaRepository<AssetTag, Long>,AssetTagRepositoryCustom{
}
