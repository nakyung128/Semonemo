package com.semonemo.presentation.screen.store.subScreen

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface StoreFullUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : StoreFullUiEvent
}
