package com.semonemo.domain.model

data class Auction(
    val id: Long = 0L,
    val status: String = "",
    val nftId: Long = 0L,
    val registerId: Long = 0L,
    val nftImageUrl: String = "",
    val participants: Int = 0,
    val startPrice: Long = 0L,
    val currentBid: Long = 0L,
    val finalPrice: Long = 0L,
    val startTime: String? = null,
    val endTime: String? = null,
)
