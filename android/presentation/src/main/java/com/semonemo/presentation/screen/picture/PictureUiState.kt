package com.semonemo.presentation.screen.picture

import android.graphics.Bitmap
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.presentation.screen.frame.FrameType

data class PictureUiState(
    val isLoading: Boolean = false,
    val bitmaps: List<Bitmap> = listOf(),
    val type: FrameType = FrameType.OneByOne,
    val frames: List<MyFrame> = listOf(),
)
