package com.semonemo.presentation.screen.signup

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface SignUpUiEvent {
    @Immutable
    data object SignUpSuccess : SignUpUiEvent

    @Immutable
    data class Error(
        val code: String,
        val message: String,
    ) : SignUpUiEvent
}
