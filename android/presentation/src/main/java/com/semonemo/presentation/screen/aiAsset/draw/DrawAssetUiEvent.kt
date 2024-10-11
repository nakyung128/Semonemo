package com.semonemo.presentation.screen.aiAsset.draw

sealed interface DrawAssetUiEvent {
    data class Error(
        val errorMessage: String,
    ) : DrawAssetUiEvent

    data class NavigateTo(
        val imageUrl: String,
    ) : DrawAssetUiEvent
}
