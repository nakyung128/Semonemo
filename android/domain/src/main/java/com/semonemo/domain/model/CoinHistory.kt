package com.semonemo.domain.model

data class CoinHistory(
    val logId: Long = 0L,
    val tradeId: Long = 0L,
    val fromUser: User? = null,
    val toUser: User? = null,
    val tradeType: String = "NFT 판매",
    val amount: Long = 1000L,
    val createdAt: String = "2024-10-06T11:59:50",
)
