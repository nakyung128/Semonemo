package com.semonemo.presentation.base

sealed interface BaseUiEvent {
    data object Logout : BaseUiEvent

    data object Login : BaseUiEvent

    data class Error(
        val code: String,
        val message: String,
    ) : BaseUiEvent
}
