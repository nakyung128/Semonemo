package com.semonemo.presentation.screen.wallet

import com.semonemo.domain.model.Coin
import com.semonemo.domain.model.CoinHistory

data class WalletUiState(
    val coinHistory: List<CoinHistory> = listOf(),
    val userCoin: Coin = Coin(),
    val coinPrice: Long = 0L,
    val isLoading: Boolean = false,
    val nickname: String = "",
    val userId: Long = -1L,
    val coinChanged: Double = 0.0,
)
