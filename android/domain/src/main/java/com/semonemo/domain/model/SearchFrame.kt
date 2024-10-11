package com.semonemo.domain.model

data class SearchFrame(
    val content: List<FrameDetail> = emptyList(),
    val pageable: Pageable = Pageable(),
    val last: Boolean = false,
    val totalElements: Int = 0,
    val totalPages: Int = 0,
    val size: Int = 0,
    val number: Int = 0,
    val sort: Sort = Sort(),
    val first: Boolean = false,
    val numberOfElements: Int = 0,
    val empty: Boolean = false,
)

data class FrameDetail(
    val marketId: Long = 0,
    val nftId: Long = 0,
    val seller: User = User(),
    val price: Long = 0L,
    val likeCount: Long = 0,
    val isLiked: Boolean = false,
    val nftInfo: FrameInfo = FrameInfo(),
    val tags: List<String> = emptyList(),
)

data class FrameInfo(
    val tokenId: Long = 0,
    val creator: String = "",
    val currentOwner: String = "",
    val data: NftData = NftData(),
)

data class NftData(
    val title: String = "",
    val content: String = "",
    val image: String = "",
)
