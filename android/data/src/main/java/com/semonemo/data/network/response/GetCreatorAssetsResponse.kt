package com.semonemo.data.network.response

import com.semonemo.domain.model.User

data class GetCreatorAssetsResponse(
    val content: List<CreatorAsset> = listOf(),
    val nextCursor: Long? = null,
    val hasNext: Boolean = false,

)

data class CreatorAsset(
    val assetId: Long = 0L,
    val assetSellId: Long = 0L,
    val creator: User = User(),
    val imageUrl: String = "",
    val createdAt: String = "",
    val hits: Long = 0L,
    val likedCount: Long = 0L,
    val price: Long = 0L,
    val isLiked: Boolean = false,
)