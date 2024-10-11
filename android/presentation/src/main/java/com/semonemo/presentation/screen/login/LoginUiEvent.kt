package com.semonemo.presentation.screen.login

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable

@Stable
sealed interface LoginUiEvent {
    @Immutable
    data object AutoLogin : LoginUiEvent

    @Immutable
    data class RequiredRegister(
        val walletAddress: String,
    ) : LoginUiEvent

    @Immutable
    data class Error(
        val errorCode: String = "",
        val errorMessage: String = "",
    ) : LoginUiEvent

    @Immutable
    data object LoginSuccess : LoginUiEvent
}
