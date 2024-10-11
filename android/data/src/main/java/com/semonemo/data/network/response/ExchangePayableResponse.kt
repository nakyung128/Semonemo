package com.semonemo.data.network.response

data class ExchangePayableResponse(
    val userId: Long = 0L,
    val coinBalance: Long = 0L,
    val payableBalance: Long = 0L,
)
