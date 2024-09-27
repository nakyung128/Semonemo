package com.semonemo.spring_server.domain.asset.repository.assetsell;

import java.util.List;

import com.semonemo.spring_server.domain.asset.model.AssetSell;

public interface AssetSellRepositoryCustom {
	List<AssetSell> findTopN(Long nowId,String orderBy, int size);

	List<AssetSell> findNextN(Long nowId,String orderBy, Long cursorId, int size);

	AssetSell findByAssetId(Long assetId);

	void updateCount(int count, Long assetSellId);

	void plusHits(Long assetSellId);

}
