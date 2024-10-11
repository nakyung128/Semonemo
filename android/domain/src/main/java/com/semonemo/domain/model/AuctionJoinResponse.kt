package com.semonemo.domain.model

data class AuctionJoinResponse(
    val anonym: Int = 0,
    val participants: Int = 0,
    val bidLogs: List<AuctionBidLogServer> = listOf(),
)

data class AuctionBidLogServer(
    val userId: Long = 0L,
    val anonym: Int = 0,
    val bidAmount: Long = 0,
    val bidTime: String = "",
    val endTime: String = "",
)

data class AuctionJoin(
    val anonym: Int = 0,
    val participants: Int = 0,
    val bidLogs: List<AuctionBidLog> = listOf(),
)
