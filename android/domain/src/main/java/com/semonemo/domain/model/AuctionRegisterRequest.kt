package com.semonemo.domain.model

data class AuctionRegisterRequest(
    val nftId: Long = 0L,
    val startPrice: Long = 0,
    val txHash: String = "",
)
