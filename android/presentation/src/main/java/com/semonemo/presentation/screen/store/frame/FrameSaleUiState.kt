package com.semonemo.presentation.screen.store.frame

import com.semonemo.domain.model.myFrame.MyFrame
import java.math.BigInteger

data class FrameSaleUiState(
    val isLoading: Boolean = false,
    val frames: List<MyFrame> = listOf(),
    val price: BigInteger = BigInteger.ZERO,
    val selectFrame: MyFrame? = null,
)
