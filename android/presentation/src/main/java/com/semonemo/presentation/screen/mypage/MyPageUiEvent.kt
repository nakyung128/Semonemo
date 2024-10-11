package com.semonemo.presentation.screen.mypage

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface MyPageUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : MyPageUiEvent

    @Immutable
    data object Subscribe : MyPageUiEvent
}
