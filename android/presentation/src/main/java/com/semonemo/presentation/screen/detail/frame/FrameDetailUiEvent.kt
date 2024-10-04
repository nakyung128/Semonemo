package com.semonemo.presentation.screen.detail.frame

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface FrameDetailUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : FrameDetailUiEvent
}
