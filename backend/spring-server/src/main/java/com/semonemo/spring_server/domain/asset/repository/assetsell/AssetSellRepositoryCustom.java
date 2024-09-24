package com.semonemo.spring_server.domain.asset.repository.assetsell;

import java.util.List;

import com.semonemo.spring_server.domain.asset.model.AssetSell;

public interface AssetSellRepositoryCustom {
	List<AssetSell> findTopN(Long nowId, int i);

	List<AssetSell> findNextN(Long nowId, Long cursorId, int i);

	AssetSell findByAssetId(Long assetId);

	void updateCount(int count, Long assetSellId);
}
