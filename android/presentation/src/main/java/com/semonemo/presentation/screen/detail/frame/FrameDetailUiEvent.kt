package com.semonemo.presentation.screen.detail.frame

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.semonemo.domain.model.Coin

@Stable
sealed interface FrameDetailUiEvent {
    @Immutable
    data class Error(
        val errorMessage: String,
    ) : FrameDetailUiEvent

    @Immutable
    data class LoadCoin(
        val coin: Coin,
    ) : FrameDetailUiEvent

    @Immutable
    data object Success : FrameDetailUiEvent

    @Immutable
    data class CancelSale(
        val errorMessage: String,
    ) : FrameDetailUiEvent
}
