package com.semonemo.domain.request

data class SellNftRequest(
    val nftId: Long,
    val price: Long,
    val txHash: String,
)
