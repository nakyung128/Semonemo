package com.semonemo.presentation.screen.detail.frame

import androidx.compose.runtime.Stable
import com.semonemo.domain.model.FrameDetail

@Stable
data class FrameDetailUiState(
    val isLoading: Boolean = false,
    val frame: FrameDetail = FrameDetail(),
    val isLiked: Boolean = false,
    val likedCount: Long = 0L,
    val creatorFrames: List<FrameDetail> = listOf(),
    val userId: Long = 0L,
)
