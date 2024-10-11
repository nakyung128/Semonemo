package com.semonemo.presentation.screen.aiAsset.prompt

import com.semonemo.domain.request.makeAiAsset.PaintingStyle

data class PromptUiState(
    val isLoading: Boolean = false,
    val style: PaintingStyle = PaintingStyle.Realistic.People,
)
