package com.semonemo.presentation.screen.aiAsset.prompt

import android.net.Uri
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.request.makeAiAsset.MakeAiAssetRequest
import com.semonemo.domain.request.makeAiAsset.OverrideSettings
import com.semonemo.domain.request.makeAiAsset.PaintingStyle
import com.semonemo.presentation.base.BaseViewModel
import com.semonemo.presentation.util.decodeBase64ToImage
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PromptViewModel
    @Inject
    constructor(
        private val aiRepository: AiRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(PromptUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<PromptUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun updateStyle(
            type: String,
            what: String,
        ) {
            val paintingStyle =
                if (type == "실사") {
                    when (what) {
                        "사람" -> PaintingStyle.Realistic.People
                        "동물" -> PaintingStyle.Realistic.Animal
                        else -> PaintingStyle.Realistic.Ect
                    }
                } else if (type == "카툰") {
                    when (what) {
                        "사람" -> PaintingStyle.Cartoon.People
                        "동물" -> PaintingStyle.Cartoon.Animal
                        else -> PaintingStyle.Cartoon.Ect
                    }
                } else {
                    when (what) {
                        "사람" -> PaintingStyle.Anime.People
                        "동물" -> PaintingStyle.Anime.Animal
                        else -> PaintingStyle.Anime.Ect
                    }
                }

            _uiState.update {
                it.copy(
                    style = paintingStyle,
                )
            }
        }

        fun makePromptAsset(prompt: String) {
            viewModelScope.launch {
                aiRepository
                    .makeAiAsset(
                        request =
                            MakeAiAssetRequest(
                                prompt = uiState.value.style.prompt + ", draw only $prompt",
                                negativePrompt = uiState.value.style.negativePrompt,
                                overrideSettings =
                                    OverrideSettings(
                                        sdModelCheckpoint = uiState.value.style.model,
                                    ),
                            ),
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(PromptUiEvent.Error(errorMessage = response.errorMessage))
                            is ApiResponse.Success ->
                                _uiEvent.emit(
                                    PromptUiEvent.NavigateTo(
                                        Uri.encode(
                                            decodeBase64ToImage(response.data).toString(),
                                        ),
                                    ),
                                )
                        }
                    }
            }
        }
    }
