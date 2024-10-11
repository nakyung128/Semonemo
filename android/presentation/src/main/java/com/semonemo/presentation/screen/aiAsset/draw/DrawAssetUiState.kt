package com.semonemo.presentation.screen.aiAsset.draw

import android.net.Uri
import com.semonemo.domain.request.makeAiAsset.PaintingStyle

data class DrawAssetUiState(
    val isLoading: Boolean = false,
    val imgUrl: Uri? = null,
    val style: PaintingStyle = PaintingStyle.Realistic.People,
)
