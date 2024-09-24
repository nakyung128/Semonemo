package com.semonemo.spring_server.domain.asset.repository.assetimage;

import java.util.List;

import com.querydsl.jpa.JPAExpressions;
import com.semonemo.spring_server.domain.asset.model.AssetImage;

public interface AssetImageRepositoryCustom {
	 List<AssetImage> findByUserIdTopN(Long nowId, int size);

	 List<AssetImage> findByUserIdNextN(Long nowId, Long cursorId, int size);

	List<AssetImage> findByCreatorTopN(Long nowid,Long userid, int size);

	List<AssetImage> findByCreatorIdNextN(Long nowid, Long userid, Long cursorId, int size);
}