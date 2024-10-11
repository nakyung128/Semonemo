package com.semonemo.presentation.screen.coin

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface CoinUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : CoinUiEvent
}
