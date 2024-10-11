package com.semonemo.presentation.screen.imgAsset

import android.net.Uri
import com.semonemo.domain.request.makeAiAsset.PaintingStyle

data class ImageSelectUiState(
    val imageUrl: Uri? = null,
    val isLoading: Boolean = false,
    val style: PaintingStyle = PaintingStyle.None(),
)
