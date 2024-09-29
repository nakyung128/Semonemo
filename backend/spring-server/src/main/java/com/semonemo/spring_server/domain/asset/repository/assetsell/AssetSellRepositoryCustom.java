package com.semonemo.spring_server.domain.asset.repository.assetsell;

import java.util.List;

import com.semonemo.spring_server.domain.asset.model.AssetSell;

public interface AssetSellRepositoryCustom {
	List<AssetSell> findTopN( int size);

	List<AssetSell> findNextN(Long cursorId, int size);

	AssetSell findByAssetId(Long assetId);

	void updateCount(int count, Long assetSellId);

	void plusHits(Long assetSellId);

}
