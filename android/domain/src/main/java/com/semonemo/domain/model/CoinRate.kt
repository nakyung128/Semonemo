package com.semonemo.domain.model

data class CoinRate(
    val createdAt: String = "",
    val price: Long = 0L,
    val changed: Double = 0.0,
    val id: Long = 0L,
)
