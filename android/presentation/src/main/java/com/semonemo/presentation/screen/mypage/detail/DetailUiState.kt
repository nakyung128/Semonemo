package com.semonemo.presentation.screen.mypage.detail

import androidx.compose.runtime.Stable

@Stable
data class DetailUiState(
    val userId: Long = 0L,
    val ownerId: Long = 0L,
    val owner: String = "",
    val profileImg: String = "",
    val tags: List<String> = listOf(),
    val isOpen: Boolean = false,
    val isOnSale: Boolean = false,
    val title: String = "",
    val content: String = "",
    val image: String = "",
    val isLoading: Boolean = false,
    val marketId: Long = 0L,
    val tokenId: Long = 0L,
    val nftId: Long = 0L,
)
