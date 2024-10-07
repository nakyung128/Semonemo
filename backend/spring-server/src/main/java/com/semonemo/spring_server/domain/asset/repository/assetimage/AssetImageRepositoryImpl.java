package com.semonemo.spring_server.domain.asset.repository.assetimage;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.QAssetImage;
import com.semonemo.spring_server.domain.asset.model.QAssetPurchase;
import com.semonemo.spring_server.domain.asset.model.QAssetSell;

public class AssetImageRepositoryImpl implements AssetImageRepositoryCustom {
	private final JPAQueryFactory queryFactory;

	public AssetImageRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	QAssetImage assetImage = QAssetImage.assetImage;
	QAssetPurchase assetPurchase = QAssetPurchase.assetPurchase;
	QAssetSell assetSell = QAssetSell.assetSell;

	@Override
	public List<AssetImage> findByUserIdTopN(Long nowId, int size) {
		return queryFactory
			.selectFrom(assetImage)
			.where(
				assetImage.creator.eq(nowId)
					.or(assetImage.Id.in(
						JPAExpressions
							.select(assetSell.assetId)
							.from(assetSell)
							.where(assetSell.Id.in(
								JPAExpressions
									.select(assetPurchase.assetSellId)
									.from(assetPurchase)
									.where(assetPurchase.userId.eq(nowId))
							))
					))
			)
			.orderBy(assetImage.Id.desc())
			.limit(size)
			.fetch();
	}

	@Override
	public List<AssetImage> findByUserIdNextN(Long nowId, Long cursorId, int size) {
		return queryFactory
			.selectFrom(assetImage)
			.where(
				assetImage.Id.lt(cursorId)
					.and(
						assetImage.creator.eq(nowId)
							.or(assetImage.Id.in(
								JPAExpressions
									.select(assetSell.assetId)
									.from(assetSell)
									.where(assetSell.Id.in(
										JPAExpressions
											.select(assetPurchase.assetSellId)
											.from(assetPurchase)
											.where(assetPurchase.userId.eq(nowId))
									))
							))
					)
			)
			.orderBy(assetImage.Id.desc())
			.limit(size)
			.fetch();
	}

}
