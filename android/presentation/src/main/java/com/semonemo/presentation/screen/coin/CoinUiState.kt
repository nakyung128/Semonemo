package com.semonemo.presentation.screen.coin

import com.semonemo.domain.model.WeeklyCoin

data class CoinUiState(
    val coinPrice: Long = 0L,
    val changed: Double = 0.0,
    val weeklyCoin: List<WeeklyCoin> = listOf(),
    val isLoading: Boolean = false,
)
