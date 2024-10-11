package com.semonemo.presentation.screen.picture

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface PictureUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : PictureUiEvent

    @Immutable
    data object NavigateToSelect : PictureUiEvent

    @Immutable
    data class NavigateToCamera(
        val frameIdx: Int,
    ) : PictureUiEvent

    @Immutable
    data object NoAvailableFrame : PictureUiEvent
}
