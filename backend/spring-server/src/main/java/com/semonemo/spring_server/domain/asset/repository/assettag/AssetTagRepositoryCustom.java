package com.semonemo.spring_server.domain.asset.repository.assettag;

import java.util.List;

import com.semonemo.spring_server.domain.asset.model.AssetTag;

public interface AssetTagRepositoryCustom {
	List<AssetTag> findByAssetSellId(Long assetSellId);

	List<String> findTagsByAssetSellId(Long assetSellId);
}
