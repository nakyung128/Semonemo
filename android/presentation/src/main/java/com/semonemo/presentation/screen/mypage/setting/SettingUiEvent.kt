package com.semonemo.presentation.screen.mypage.setting

sealed interface SettingUiEvent {
    data class Error(
        val message: String,
    ) : SettingUiEvent

    data object Logout : SettingUiEvent

    data class EditSuccess(
        val message: String,
    ) : SettingUiEvent
}
