package com.semonemo.presentation.screen.detail.asset

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AssetDetailUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : AssetDetailUiEvent
}
