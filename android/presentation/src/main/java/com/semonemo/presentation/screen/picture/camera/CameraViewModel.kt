package com.semonemo.presentation.screen.picture.camera

import android.graphics.Bitmap
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.NftRepository
import com.semonemo.presentation.screen.picture.PictureUiEvent
import com.semonemo.presentation.screen.picture.PictureUiState
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
class CameraViewModel
    @Inject
    constructor(
        private val nftRepository: NftRepository,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(PictureUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<PictureUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun takePhoto(
            bitmap: Bitmap,
            amount: Int,
        ) {
            _uiState.update {
                val newList =
                    it.bitmaps.toMutableList().apply {
                        add(bitmap)
                    }
                it.copy(
                    bitmaps = newList,
                )
            }
            if (amount == 1) {
                viewModelScope.launch {
                    _uiEvent.emit(PictureUiEvent.NavigateToSelect)
                }
            }
        }

        fun loadAvailableFrames(type: Int) {
            viewModelScope.launch {
                nftRepository
                    .getAvailableNft(type)
                    .onStart {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }.onCompletion {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(PictureUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> {
                                _uiState.update {
                                    it.copy(
                                        frames = response.data,
                                    )
                                }
                            }
                        }
                    }
            }
        }

        fun resetUiState() {
            _uiState.value = PictureUiState()
        }
    }
