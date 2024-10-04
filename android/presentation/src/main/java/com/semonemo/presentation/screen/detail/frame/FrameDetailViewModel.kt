package com.semonemo.presentation.screen.detail.frame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.NftRepository
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
class FrameDetailViewModel
    @Inject
    constructor(
        private val nftRepository: NftRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FrameDetailUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<FrameDetailUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            getFrameDetail(savedStateHandle["marketId"] ?: -1L)
        }

        private fun getFrameDetail(marketId: Long) {
            viewModelScope.launch {
                nftRepository
                    .getFrameDetail(marketId)
                    .onStart {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(FrameDetailUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                _uiState.update { it.copy(frame = response.data) }
                            }
                        }
                    }
            }
        }
    }
