package com.semonemo.data.network.response

import com.semonemo.domain.model.AuctionBidLog

data class AuctionBidLogList(
    val content: List<AuctionBidLog> = listOf(),
)
