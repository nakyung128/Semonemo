package com.semonemo.presentation.screen.detail.asset

import androidx.compose.runtime.Stable
import com.semonemo.domain.model.SellAssetDetail

@Stable
data class AssetDetailUiState(
    val isLoading: Boolean = false,
    val asset: SellAssetDetail = SellAssetDetail(),
    val creatorAssets: List<SellAssetDetail> = listOf(),
    val isLiked: Boolean = false,
    val likedCount: Long = 0L,
    val userId: Long = 0L,
)
