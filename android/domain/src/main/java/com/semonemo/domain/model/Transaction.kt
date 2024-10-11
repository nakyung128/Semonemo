package com.semonemo.domain.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Transaction(
    @SerialName("from")
    val from: String,
    @SerialName("to")
    val to: String,
    @SerialName("gas")
    val gas: String,
    @SerialName("gasPrice")
    val gasPrice: String,
    @SerialName("data")
    val data: String,
    @SerialName("nonce")
    val nonce: String,
)
