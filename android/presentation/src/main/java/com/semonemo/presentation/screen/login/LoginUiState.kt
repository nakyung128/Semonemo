package com.semonemo.presentation.screen.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LoginUiState {
    @Immutable
    data object Init : LoginUiState

    @Immutable
    data class Loading(
        val isWalletLoading: Boolean = false,
        val walletAddress: String = "",
    ) : LoginUiState
}
