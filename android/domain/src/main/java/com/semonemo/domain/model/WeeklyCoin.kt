package com.semonemo.domain.model

data class WeeklyCoin(
    val date: String = "",
    val averagePrice: Double = 0.0,
    val dailyChange: Double = 0.0,
    val highestPrice: Long = 0L,
    val lowestPrice: Long = 0L,
)
