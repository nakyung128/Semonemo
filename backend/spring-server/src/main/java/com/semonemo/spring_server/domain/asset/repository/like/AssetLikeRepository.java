package com.semonemo.spring_server.domain.asset.repository.like;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.semonemo.spring_server.domain.asset.model.AssetLike;

@Repository
public interface AssetLikeRepository extends JpaRepository<AssetLike, Long>, AssetLikeRepositoryCustom {

	AssetLike findByUserIdAndAssetSellId(Long nowid, Long assetSellId);

	boolean existsByUserIdAndAssetSellId(Long nowid, Long assetSellId);
}
