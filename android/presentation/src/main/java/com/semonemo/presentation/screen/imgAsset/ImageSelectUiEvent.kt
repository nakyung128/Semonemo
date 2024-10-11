package com.semonemo.presentation.screen.imgAsset

sealed interface ImageSelectUiEvent {
    data class Error(
        val errorMessage: String,
    ) : ImageSelectUiEvent

    data class NavigateTo(
        val imageUrl: String,
    ) : ImageSelectUiEvent
}
