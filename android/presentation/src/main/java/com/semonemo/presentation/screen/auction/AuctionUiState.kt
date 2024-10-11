package com.semonemo.presentation.screen.auction

import androidx.compose.runtime.Stable
import com.semonemo.domain.model.Auction

@Stable
data class AuctionUiState(
    val readyAuctionList: List<Auction> = listOf(),
    val progressAuctionList: List<Auction> = listOf(),
    val endAuctionList: List<Auction> = listOf(),
    val cancelAuctionList: List<Auction> = listOf(),
)
