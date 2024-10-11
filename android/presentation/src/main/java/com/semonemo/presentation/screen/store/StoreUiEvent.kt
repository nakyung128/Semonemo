package com.semonemo.presentation.screen.store

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface StoreUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : StoreUiEvent
}
