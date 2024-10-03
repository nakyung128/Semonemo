package com.semonemo.domain.model

data class Asset(
    val assetId: Long = 0L,
    val imageUrl: String = "",
    val creator: Long = 0L,
    val isLiked: Boolean = false,
)

data class SellAsset(
    val assetId: Long = 0,
    val price: Int = 0,
    val tags: List<String>,
)

data class SellAssetDetail(
    val assetId: Long = 0,
    val assetSellId: Long = 0,
    val creator: Long = 0,
    val imageUrl: String = "",
    val createAt: String = "",
    val hits: Long = 0,
    val likeCount: Long = 0,
    val nickname: String = "",
    val price: Long = 0,
    val isLiked: Boolean = false,
    val tags: List<String> = emptyList(),
)

data class CreateAsset(
    val content: List<Asset> = emptyList(),
    val nextCursor: Long? = null,
    val hasNext: Boolean = false,
)
