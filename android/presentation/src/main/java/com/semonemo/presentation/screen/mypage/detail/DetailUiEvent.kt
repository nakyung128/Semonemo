package com.semonemo.presentation.screen.mypage.detail

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface DetailUiEvent {
    @Immutable
    data object Loading : DetailUiEvent

    @Immutable
    data class OpenSuccess(
        val message: String = "",
    ) : DetailUiEvent

    @Immutable
    data class Error(
        val errorMessage: String = "",
    ) : DetailUiEvent
}
