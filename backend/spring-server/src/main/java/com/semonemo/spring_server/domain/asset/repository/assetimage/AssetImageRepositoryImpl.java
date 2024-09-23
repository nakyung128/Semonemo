package com.semonemo.spring_server.domain.asset.repository.assetimage;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.querydsl.jpa.impl.JPAQueryFactory;
import com.semonemo.spring_server.domain.asset.model.AssetImage;
import com.semonemo.spring_server.domain.asset.model.QAssetImage;

public class AssetImageRepositoryImpl implements AssetImageRepositoryCustom {
	private final JPAQueryFactory queryFactory;
	public AssetImageRepositoryImpl(JPAQueryFactory queryFactory) {
		this.queryFactory = queryFactory;
	}

	QAssetImage assetImage = QAssetImage.assetImage;


	// @Override
	// public List<AssetImage> findByUserIdTopN(Long nowId, Long userId, int limit) {
	//
	// 	return queryFactory
	// 		.selectFrom(assetImage)
	// 		.where(assetImage.userId.eq(userId)
	// 			.and(boards.isBoardOpen.isTrue().or(
	// 				boards.isBoardOpen.isFalse().and(
	// 					JPAExpressions
	// 						.selectOne()
	// 						.from(QFollow.follow)
	// 						.where(QFollow.follow.follower.userId.eq(boards.userId)
	// 							.and(QFollow.follow.followee.userId.eq(nowId)))
	// 						.exists()
	// 				)
	// 			)))
	// 		.orderBy(boards.boardId.desc())
	// 		.limit(limit)
	// 		.fetch();
	// }
	//
	// @Override
	// public  List<Boards> findByUserIdNextN(Long nowId, Long userId, Long cursorId, int limit) {
	// 	return queryFactory
	// 		.selectFrom(boards)
	// 		.where(boards.userId.eq(userId)
	// 			.and(boards.boardId.lt(cursorId))
	// 			.and(boards.isBoardOpen.isTrue().or(
	// 				boards.isBoardOpen.isFalse().and(
	// 					JPAExpressions
	// 						.selectOne()
	// 						.from(QFollow.follow)
	// 						.where(QFollow.follow.follower.userId.eq(boards.userId)
	// 							.and(QFollow.follow.followee.userId.eq(nowId)))
	// 						.exists()
	// 				)
	// 			)))
	// 		.orderBy(boards.boardId.desc())
	// 		.limit(limit)
	// 		.fetch();
	// }
}
