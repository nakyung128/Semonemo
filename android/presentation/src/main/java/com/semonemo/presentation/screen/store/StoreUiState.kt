package com.semonemo.presentation.screen.store

import androidx.compose.runtime.Stable
import com.semonemo.domain.model.FrameDetail

@Stable
data class StoreUiState(
    val isLoading : Boolean = false,
    val hotFrame: List<FrameDetail> = listOf(),
    val saleFrame: List<FrameDetail> = listOf(),
    val saleAsset: List<FrameDetail> = listOf(),
)
