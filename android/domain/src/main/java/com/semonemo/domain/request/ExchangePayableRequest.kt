package com.semonemo.domain.request

data class ExchangePayableRequest(
    val txHash: String,
    val amount: Long,
)
