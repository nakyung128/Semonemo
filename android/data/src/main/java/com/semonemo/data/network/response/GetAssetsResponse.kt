package com.semonemo.data.network.response

import com.semonemo.domain.model.Asset

data class GetAssetsResponse(
    val content: List<Asset> = listOf(),
    val nextCursor: Long? = null,
    val hasNext: Boolean? = null,
)
