package com.semonemo.domain.model

data class Nft(
    val nftId: Long = 0,
    val creator: User = User(),
    val owner: User = User(),
    val tokenId: String = "",
    val tags: List<String> = listOf(),
    val isOpen: Boolean = true,
    val isOnSale: Boolean = true,
    val nftInfo: NftInfo = NftInfo(),
)

data class NftInfo(
    val tokenId: Long = 0,
    val creator: String = "",
    val currentOwner: String = "",
    val tokenUrl: String = "",
)
