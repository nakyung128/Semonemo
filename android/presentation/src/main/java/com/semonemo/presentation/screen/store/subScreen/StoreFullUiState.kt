package com.semonemo.presentation.screen.store.subScreen

import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.SellAssetDetail

data class StoreFullUiState(
    val isLoading: Boolean = true,
    val frameList: List<FrameDetail> = listOf(),
    val assetList: List<SellAssetDetail> = listOf(),
)
