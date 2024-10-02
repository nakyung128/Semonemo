package com.semonemo.spring_server.domain.asset.repository.assetsell;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.semonemo.spring_server.domain.asset.model.AssetSell;
@Repository
public interface AssetSellRepository extends JpaRepository<AssetSell, Long>,AssetSellRepositoryCustom {
	boolean existsByAssetId(Long assetId);
}
