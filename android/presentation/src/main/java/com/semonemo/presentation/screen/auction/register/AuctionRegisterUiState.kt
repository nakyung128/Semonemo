package com.semonemo.presentation.screen.auction.register

import com.semonemo.domain.model.myFrame.MyFrame

data class AuctionRegisterUiState(
    val isLoading: Boolean = false,
    val frames: List<MyFrame> = listOf(),
    val price: Long = 0L,
    val selectFrame: MyFrame? = null,
)
