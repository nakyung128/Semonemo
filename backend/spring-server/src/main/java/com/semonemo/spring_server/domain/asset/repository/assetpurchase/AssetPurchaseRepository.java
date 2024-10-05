package com.semonemo.spring_server.domain.asset.repository.assetpurchase;

import org.springframework.data.jpa.repository.JpaRepository;

import com.semonemo.spring_server.domain.asset.model.AssetPurchase;

public interface AssetPurchaseRepository extends JpaRepository<AssetPurchase, Long> {
}
