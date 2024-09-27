package com.semonemo.spring_server.domain.asset.repository.assetimage;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.semonemo.spring_server.domain.asset.model.AssetImage;

@Repository
public interface AssetImageRepository extends JpaRepository<AssetImage, Long>,AssetImageRepositoryCustom {
}
