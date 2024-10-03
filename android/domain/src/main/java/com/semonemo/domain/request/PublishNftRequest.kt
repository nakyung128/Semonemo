package com.semonemo.domain.request

data class PublishNftRequest(
    val txHash: String,
    val tags: List<String>,
    val frameType: Int,
)
