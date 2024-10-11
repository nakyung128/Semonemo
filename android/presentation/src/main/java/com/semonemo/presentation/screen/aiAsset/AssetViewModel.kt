package com.semonemo.presentation.screen.aiAsset

import android.net.Uri
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.request.RemoveBgRequest
import com.semonemo.presentation.base.BaseViewModel
import com.semonemo.presentation.util.decodeBase64ToImage
import com.semonemo.presentation.util.encodeImageToBase64
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
import java.io.File
import java.util.Base64
import javax.inject.Inject

@HiltViewModel
class AssetViewModel
    @Inject
    constructor(
        private val aiRepository: AiRepository,
        private val savedStateHandle: SavedStateHandle,
        private val assetRepository: AssetRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(AssetDoneUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<AssetDoneUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            _uiState.update {
                it.copy(
                    assetUrl = savedStateHandle["assetUrl"],
                )
            }
        }

        fun removeBackground() {
            uiState.value.assetUrl?.let { assetUrl ->
                val base64String =
                    if (assetUrl.contains("data:image/png;base64,")) {
                        Uri.decode(assetUrl).replace("data:image/png;base64,", "")
                    } else {
                        encodeImageToBase64(assetUrl)
                    }
                viewModelScope.launch {
                    aiRepository
                        .removeBg(RemoveBgRequest(inputImage = base64String))
                        .onStart {
                            _uiState.update { it.copy(isLoading = true) }
                        }.onCompletion {
                            _uiState.update { it.copy(isLoading = false) }
                        }.collectLatest { response ->
                            when (response) {
                                is ApiResponse.Error ->
                                    _uiEvent.emit(
                                        AssetDoneUiEvent.Error(
                                            response.errorMessage,
                                        ),
                                    )

                                is ApiResponse.Success -> {
                                    val string = Base64.getDecoder().decode(response.data)
                                    _uiState.update {
                                        it.copy(assetUrl = decodeBase64ToImage(response.data).toString())
                                    }
                                }
                            }
                        }
                }
            }
        }

        fun uploadAsset(file: File?) {
            viewModelScope.launch {
                if (file == null) {
                    _uiEvent.emit(AssetDoneUiEvent.Error("file is not selected"))
                } else {
                    assetRepository
                        .registerAsset(file)
                        .onStart {
                            _uiState.update { it.copy(isLoading = true) }
                        }.onCompletion {
                            _uiState.update { it.copy(isLoading = false) }
                        }.collectLatest { response ->
                            when (response) {
                                is ApiResponse.Error -> _uiEvent.emit(AssetDoneUiEvent.Error(response.errorMessage))
                                is ApiResponse.Success -> _uiEvent.emit(AssetDoneUiEvent.Done)
                            }
                        }
                }
            }
        }
    }
