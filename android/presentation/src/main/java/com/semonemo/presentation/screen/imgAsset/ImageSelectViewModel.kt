package com.semonemo.presentation.screen.imgAsset

import android.net.Uri
import androidx.core.net.toUri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.request.makeAiAsset.AlwaysonScripts
import com.semonemo.domain.request.makeAiAsset.Arg
import com.semonemo.domain.request.makeAiAsset.Controlnet
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
class ImageSelectViewModel
    @Inject
    constructor(
        private val aiRepository: AiRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(ImageSelectUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<ImageSelectUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            val selectImageString = savedStateHandle.get<String>("selectedImg")
            val imageUri =
                selectImageString?.let { Uri.decode(it) }?.let { Uri.parse(it) } ?: "".toUri()
            _uiState.update {
                it.copy(
                    imageUrl = imageUri,
                )
            }
        }

        fun updateImageUri(imageUri: Uri) {
            _uiState.update {
                it.copy(imageUrl = imageUri)
            }
        }

        fun updateStyle(style: String) {
            val paintingStyle =
                when (style) {
                    "실사" -> PaintingStyle.Realistic.People
                    "카툰" -> PaintingStyle.Cartoon.People
                    "없음" -> PaintingStyle.None()
                    else -> PaintingStyle.Anime.People
                }
            _uiState.update {
                it.copy(
                    style = paintingStyle,
                )
            }
        }

        fun makeAiAsset(imageUrl: String) {
            viewModelScope.launch {
                aiRepository
                    .makeAiAsset(
                        request =
                            MakeAiAssetRequest(
                                prompt = uiState.value.style.prompt,
                                negativePrompt = uiState.value.style.negativePrompt,
                                overrideSettings =
                                    OverrideSettings(
                                        sdModelCheckpoint = uiState.value.style.model,
                                    ),
                                alwaysonScripts =
                                    AlwaysonScripts(
                                        controlnet =
                                            Controlnet(
                                                listOf(
                                                    Arg(image = imageUrl),
                                                    Arg(
                                                        model = "controlnetT2IAdapter_t2iAdapterColor [c58d39ff]",
                                                        module = "t2ia_color_grid",
                                                        image = imageUrl,
                                                    ),
                                                ),
                                            ),
                                    ),
                            ),
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(ImageSelectUiEvent.Error(errorMessage = response.errorMessage))
                            is ApiResponse.Success -> {
                                _uiEvent.emit(
                                    ImageSelectUiEvent.NavigateTo(Uri.encode(decodeBase64ToImage(response.data).toString())),
                                )
                            }
                        }
                    }
            }
        }
    }
