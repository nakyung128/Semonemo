package com.semonemo.presentation.screen.detail.asset

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.CoinRepository
import com.semonemo.domain.request.PurchaseAssetRequest
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
        private val authDataSource: AuthDataSource,
        private val coinRepository: CoinRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(AssetDetailUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<AssetDetailUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            viewModelScope.launch {
                authDataSource.getUserId()?.let { userId ->
                    _uiState.update { it.copy(userId = userId.toLong()) }
                }
            }
            getSaleNftDetail(savedStateHandle["assetSellId"] ?: -1L)
        }

        fun getBalance(price: Long) {
            viewModelScope.launch {
                coinRepository.getBalance().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AssetDetailUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            val balance = response.data.payableBalance
                            val event =
                                if (balance < price) {
                                    AssetDetailUiEvent.Error("잔액이 부족합니다!!")
                                } else {
                                    AssetDetailUiEvent.Purchase(price)
                                }
                            _uiEvent.emit(event)
                        }
                    }
                }
            }
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
                                getCreatorAssets(response.data.creator.userId, assetSellId)
                            }
                        }
                    }
            }
        }

        private suspend fun getCreatorAssets(
            userId: Long,
            assetSellId: Long,
        ) {
            assetRepository
                .getCreatorAsset(userId)
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.onCompletion {
                    _uiState.update { it.copy(isLoading = false) }
                }.collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> _uiEvent.emit(AssetDetailUiEvent.Error(response.errorMessage))
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    creatorAssets = response.data.filter { it.assetSellId != assetSellId },
                                )
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

        fun purchaseAsset(
            txHash: String,
            assetSellId: Long,
        ) {
            viewModelScope.launch {
                assetRepository
                    .purchaseAsset(
                        request =
                            PurchaseAssetRequest(
                                txHash = txHash,
                                assetSellId = assetSellId,
                            ),
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion { _uiState.update { it.copy(isLoading = false) } }
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(AssetDetailUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> _uiEvent.emit(AssetDetailUiEvent.Success)
                        }
                    }
            }
        }
    }
