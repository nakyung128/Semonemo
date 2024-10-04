package com.semonemo.presentation.screen.detail.frame

import com.semonemo.domain.model.FrameDetail

data class FrameDetailUiState(
    val isLoading: Boolean = false,
    val frame: FrameDetail = FrameDetail(),
)
