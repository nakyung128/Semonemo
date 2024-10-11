package com.semonemo.presentation.screen.auction.register

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AuctionRegisterUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : AuctionRegisterUiEvent

    @Immutable
    data object RegisterDone : AuctionRegisterUiEvent
}
