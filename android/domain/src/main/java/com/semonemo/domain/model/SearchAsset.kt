package com.semonemo.domain.model

data class SearchAsset(
    val content: List<AssetDetail> = emptyList(),
    val pageable: Pageable = Pageable(),
    val last: Boolean = false,
    val totalPages: Int = 0,
    val totalElements: Int = 0,
    val first: Boolean = false,
    val size: Int = 0,
    val sort: Sort = Sort(),
    val numberOfElements: Int = 0,
    val empty: Boolean = false,
)

data class AssetDetail(
    val assetSellId: Long = 0,
    val assetId: Long = 0,
    val creator: User = User(),
    val imageUrl: String = "",
    val price: Long = 0,
    val hits: Long = 0,
    val createdAt: String = "",
    val likeCount: Long = 0,
    val tags: List<String>,
    val isLiked: Boolean = false,
    val purchaseCount: Long = 0,
)
