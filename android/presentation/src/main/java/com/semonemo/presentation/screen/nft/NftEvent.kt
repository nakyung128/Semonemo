package com.semonemo.presentation.screen.nft

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface NftEvent {
    @Immutable
    data class SendTransaction(
        val transactionHash: String,
    ) : NftEvent

    @Immutable
    data class Error(
        val errorMessage: String,
    ) : NftEvent
}
