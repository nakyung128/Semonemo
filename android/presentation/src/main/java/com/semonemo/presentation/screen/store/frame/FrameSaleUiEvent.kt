package com.semonemo.presentation.screen.store.frame

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface FrameSaleUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : FrameSaleUiEvent

    @Immutable
    data object SaleDone : FrameSaleUiEvent
}
