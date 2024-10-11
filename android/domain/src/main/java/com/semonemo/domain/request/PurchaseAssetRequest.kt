package com.semonemo.domain.request

data class PurchaseAssetRequest(
    val txHash: String,
    val assetSellId: Long,
)
