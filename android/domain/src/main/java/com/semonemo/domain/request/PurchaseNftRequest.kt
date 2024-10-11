package com.semonemo.domain.request

data class PurchaseNftRequest(
    val txHash: String,
    val marketId: Long,
)
