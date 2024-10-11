package com.semonemo.presentation.screen.store.subScreen

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.NftRepository
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreFullViewModel
    @Inject
    constructor(
        private val nftRepository: NftRepository,
        private val assetRepository: AssetRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(StoreFullUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<StoreFullUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun loadNftList(orderBy: String) {
            viewModelScope.launch {
                nftRepository.getAllSaleNft(orderBy = orderBy).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(StoreFullUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    frameList = response.data,
                                )
                        }
                    }
                }
            }
        }

        fun loadAssetList(option: String) {
            viewModelScope.launch {
                assetRepository.getAllSellAssets(option = option).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(StoreFullUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            _uiState.value =
                                _uiState.value.copy(
                                    isLoading = false,
                                    assetList = response.data.content,
                                )
                        }
                    }
                }
            }
        }
    }
