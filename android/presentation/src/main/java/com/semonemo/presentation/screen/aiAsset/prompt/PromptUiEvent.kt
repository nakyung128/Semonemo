package com.semonemo.presentation.screen.aiAsset.prompt

import androidx.compose.runtime.Stable

@Stable
sealed interface PromptUiEvent {
    data class Error(
        val errorMessage: String,
    ) : PromptUiEvent

    data class NavigateTo(
        val imageUrl: String,
    ) : PromptUiEvent
}
