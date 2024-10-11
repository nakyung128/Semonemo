package com.semonemo.presentation.screen.aiAsset

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface AssetDoneUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : AssetDoneUiEvent

    @Immutable
    data object Done : AssetDoneUiEvent
}
