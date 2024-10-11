package com.semonemo.presentation.screen.detail.asset

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AssetDetailUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : AssetDetailUiEvent

    @Immutable
    data class Purchase(
        val price: Long,
    ) : AssetDetailUiEvent

    @Immutable
    data object Success : AssetDetailUiEvent
}
