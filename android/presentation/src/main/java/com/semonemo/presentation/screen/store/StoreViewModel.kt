package com.semonemo.presentation.screen.store

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class StoreViewModel
    @Inject
    constructor(
        private val nftRepository: NftRepository,
        private val assetRepository: AssetRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(StoreUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<StoreUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        private fun loadLikeNft() {
            viewModelScope.launch {
                nftRepository.getSaleLikeNft().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {}
                        is ApiResponse.Success -> {}
                    }
                }
            }
        }

        fun loadStoreInfo() {
            viewModelScope.launch {
                combine(
                    nftRepository.getHotNft(),
                    nftRepository.getAllSaleNft(orderBy = "latest"),
                    assetRepository.getAllSellAssets(option = "latest"),
                ) { hotFrame, saleFrame, saleAsset ->
                    var currentState = StoreUiState()

                    currentState =
                        when (hotFrame) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(StoreUiEvent.Error(hotFrame.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(hotFrame = hotFrame.data)
                            }
                        }
                    currentState =
                        when (saleFrame) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(StoreUiEvent.Error(saleFrame.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(saleFrame = saleFrame.data)
                            }
                        }

                    currentState =
                        when (saleAsset) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(StoreUiEvent.Error(saleAsset.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    saleAsset = saleAsset.data.content,
                                )
                            }
                        }
                    currentState
                }.collectLatest { updateUiState ->
                    _uiState.value = updateUiState
                }
            }
        }
    }
