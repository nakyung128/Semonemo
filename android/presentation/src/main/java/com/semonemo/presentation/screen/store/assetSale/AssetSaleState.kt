package com.semonemo.presentation.screen.store.assetSale

import androidx.compose.runtime.Immutable
import androidx.compose.runtime.Stable
import com.semonemo.domain.model.Asset

@Stable
sealed interface AssetSaleState {
    @Immutable
    data object Init : AssetSaleState

    @Immutable
    data class LoadSuccess(
        val assets: List<Asset> = emptyList(),
    ) : AssetSaleState

    @Immutable
    data object SellSuccess : AssetSaleState

    @Immutable
    data class Error(
        val message: String = "",
    ) : AssetSaleState
}
