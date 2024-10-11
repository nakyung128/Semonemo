package com.semonemo.domain.model

data class AuctionRegisterResponse(
    val id: Long = 0L,
    val nftId: Long = 0L,
    val status: String = "",
    val startPrice: Long = 0L,
    val winner: String? = null,
    val finalPrice: Long? = null,
    val startTime: String? = null,
    val endTime: String? = null,
    val createdAt: String? = null,
)
