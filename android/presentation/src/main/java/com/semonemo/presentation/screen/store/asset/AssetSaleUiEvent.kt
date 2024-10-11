package com.semonemo.presentation.screen.store.asset

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AssetSaleUiEvent {
    @Immutable
    data object SellSuccess : AssetSaleUiEvent

    @Immutable
    data class Error(
        val message: String = "",
    ) : AssetSaleUiEvent
}
