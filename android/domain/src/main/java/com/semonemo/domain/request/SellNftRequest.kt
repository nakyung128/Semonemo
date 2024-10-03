package com.semonemo.domain.request

import java.math.BigInteger

data class SellNftRequest(
    val nftId: Long,
    val price: BigInteger,
    val txHash: String,
)
