package com.semonemo.presentation.screen.wallet

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface WalletUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : WalletUiEvent

    @Immutable
    data class PaySuccess(
        val message: String,
    ) : WalletUiEvent
}
