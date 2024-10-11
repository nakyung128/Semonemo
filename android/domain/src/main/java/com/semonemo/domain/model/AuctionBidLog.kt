package com.semonemo.domain.model

import java.time.LocalDateTime

data class AuctionBidLog(
    val userId: Long = 0L,
    val anonym: Int = 0,
    val bidAmount: Long = 0,
    val bidTime: LocalDateTime = LocalDateTime.now(),
    val endTime: LocalDateTime = LocalDateTime.now(),
)
