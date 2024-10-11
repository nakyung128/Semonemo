package com.semonemo.presentation.screen.frame

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface FrameUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : FrameUiEvent

    @Immutable
    data class UploadFinish(
        val imageHash: String,
    ) : FrameUiEvent

    @Immutable
    data object NavigateMoment : FrameUiEvent
}
