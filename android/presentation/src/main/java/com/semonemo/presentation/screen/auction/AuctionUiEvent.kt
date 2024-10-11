package com.semonemo.presentation.screen.auction

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AuctionUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : AuctionUiEvent
}
