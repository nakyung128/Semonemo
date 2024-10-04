package com.semonemo.presentation.screen.mypage.detail

import androidx.compose.runtime.Stable

@Stable
data class DetailUiState(
    val owner: String = "",
    val profileImg: String = "",
    val tags: List<String> = listOf(),
    val isOpen: Boolean = false,
    val isOnSale: Boolean = false,
    val title: String = "",
    val content: String = "",
    val image: String = "",
)
