package com.semonemo.presentation.screen.store.asset

import com.semonemo.domain.model.Asset

data class AssetSaleUiState(
    val isLoading: Boolean = true,
    val assets: List<Asset> = emptyList(),
)
