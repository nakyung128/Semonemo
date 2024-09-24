package com.semonemo.spring_server.domain.asset.repository.assetsell;

import static com.semonemo.spring_server.domain.asset.model.QAssetSell.*;

import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.asset.model.AssetSell;

public class AssetSellRepositoryImpl implements AssetSellRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public AssetSellRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<AssetSell> findTopN(Long nowId, int size) {

		return queryFactory
			.selectFrom(assetSell)
			.orderBy(assetSell.assetSellId.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<AssetSell> findNextN(Long nowId, Long cursorId, int size) {
		return queryFactory
			.selectFrom(assetSell)
			.where(assetSell.assetSellId.lt(cursorId))
			.orderBy(assetSell.assetSellId.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public AssetSell findByAssetId(Long assetId) {
		return queryFactory
			.selectFrom(assetSell)
			.where(assetSell.assetId.eq(assetId))
			.fetchOne();
	}

	@Override
	public void updateCount(int count, Long assetSellId) {
		queryFactory
			.update(assetSell)
			.where(assetSell.assetSellId.eq(assetSellId))
			.set(assetSell.likeCount, assetSell.likeCount.add(count))
			.execute();
	}

}