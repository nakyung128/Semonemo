package com.semonemo.domain.request

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class TransferRequest(
    @SerialName("fromAddress")
    val fromAddress: String = "",
    @SerialName("toAddress")
    val toAddress: String = "",
    @SerialName("amount")
    val amount: String = "",
)
