package com.semonemo.spring_server.domain.asset.repository.assetsell;

import static com.semonemo.spring_server.domain.asset.model.QAssetSell.*;

import java.util.ArrayList;
import java.util.List;

import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.asset.model.AssetSell;

public class AssetSellRepositoryImpl implements AssetSellRepositoryCustom {

	private final JPAQueryFactory queryFactory;

	public AssetSellRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	@Override
	public List<AssetSell> findTopN(Long nowId,String orderBy, int size) {
		List<AssetSell> assetSells=new ArrayList<>();
		if(orderBy.equals("latest")){
			assetSells=queryFactory
				.selectFrom(assetSell)
				.orderBy(assetSell.Id.desc())
				.limit(size)
				.fetch();
		}
		else if(orderBy.equals("oldest")){
			assetSells=queryFactory
				.selectFrom(assetSell)
				.orderBy(assetSell.Id.asc())
				.limit(size)
				.fetch();
		}
		return assetSells;
	}

	@Override
	public List<AssetSell> findNextN(Long nowId,String orderBy, Long cursorId, int size) {
		List<AssetSell> assetSells=new ArrayList<>();
		if(orderBy.equals("latest")){
			assetSells=queryFactory
				.selectFrom(assetSell)
				.where(assetSell.Id.lt(cursorId))
				.orderBy(assetSell.Id.desc())
				.limit(size)
				.fetch();
		}
		else if(orderBy.equals("oldest")){
			assetSells=queryFactory
				.selectFrom(assetSell)
				.where(assetSell.Id.lt(cursorId))
				.orderBy(assetSell.Id.asc())
				.limit(size)
				.fetch();
		}
			return assetSells;
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
			.where(assetSell.Id.eq(assetSellId))
			.set(assetSell.likeCount, assetSell.likeCount.add(count))
			.execute();
	}

	@Override
	public void plusHits(Long assetSellId) {
		queryFactory
			.update(assetSell)
			.where(assetSell.Id.eq(assetSellId))
			.set(assetSell.hits, assetSell.hits.add(1))
			.execute();
	}



}