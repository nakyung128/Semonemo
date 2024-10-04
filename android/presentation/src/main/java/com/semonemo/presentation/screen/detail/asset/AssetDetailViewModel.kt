package com.semonemo.presentation.screen.detail.asset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AssetRepository
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
class AssetDetailViewModel
    @Inject
    constructor(
        private val assetRepository: AssetRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(AssetDetailUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<AssetDetailUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            getSaleNftDetail(savedStateHandle["assetSellId"] ?: -1L)
        }

        private fun getSaleNftDetail(assetSellId: Long) {
            viewModelScope.launch {
                assetRepository
                    .getSaleAssetDetail(assetSellId)
                    .onStart {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(AssetDetailUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                _uiState.update {
                                    it.copy(
                                        asset = response.data,
                                        isLiked = response.data.isLiked,
                                        likedCount = response.data.likeCount,
                                    )
                                }
                            }
                        }
                    }
            }
        }

        fun onLikedAsset(isLiked: Boolean) {
            _uiState.update { it.copy(isLiked = isLiked) }
            val assetSellId = uiState.value.asset.assetSellId
            viewModelScope.launch {
                if (isLiked) {
                    likeAsset(assetSellId)
                } else {
                    disLikeAsset(assetSellId)
                }
            }
        }

        private suspend fun likeAsset(assetSellId: Long) {
            assetRepository.likeAsset(assetSellId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _uiEvent.emit(AssetDetailUiEvent.Error(response.errorMessage))
                    }

                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(likedCount = response.data) }
                    }
                }
            }
        }

        private suspend fun disLikeAsset(assetSellId: Long) {
            assetRepository.unlikeAsset(assetSellId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _uiEvent.emit(AssetDetailUiEvent.Error(response.errorMessage))
                    }

                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(likedCount = response.data) }
                    }
                }
            }
        }
    }
