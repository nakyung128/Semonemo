package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import androidx.compose.runtime.Stable

@Stable
data class FrameUiState(
    val bitmap: Bitmap? = null,
    val isLoading: Boolean = false,
    val title: String = "",
    val content: String = "",
    val tags: List<String> = listOf(),
    val frameType: FrameType = FrameType.OneByOne,
)
