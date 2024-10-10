package com.semonemo.presentation.screen.detail.frame

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.CoinRepository
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.request.PurchaseNftRequest
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
        private val coinRepository: CoinRepository,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(FrameDetailUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<FrameDetailUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            viewModelScope.launch {
                authDataSource.getUserId()?.let { userId ->
                    _uiState.update { it.copy(userId = userId.toLong()) }
                }
            }
            getSaleNftDetail(savedStateHandle["nftId"] ?: -1L)
        }

        fun getBalance(price: Long) {
            viewModelScope.launch {
                coinRepository.getBalance().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(FrameDetailUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            val balance = response.data.payableBalance
                            val event =
                                if (balance < price) {
                                    FrameDetailUiEvent.Error("잔액이 부족합니다!!")
                                } else {
                                    FrameDetailUiEvent.LoadCoin(response.data)
                                }
                            _uiEvent.emit(event)
                        }
                    }
                }
            }
        }

        private fun getSaleNftDetail(marketId: Long) {
            viewModelScope.launch {
                nftRepository
                    .getSaleNftDetail(marketId)
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
                                _uiState.update {
                                    getCreatorSaleNft(response.data.seller.userId, marketId)
                                    it.copy(
                                        frame = response.data,
                                        isLiked = response.data.isLiked,
                                        likedCount = response.data.likeCount,
                                    )
                                }
                            }
                        }
                    }
            }
        }

        private suspend fun getCreatorSaleNft(
            creator: Long,
            marketId: Long,
        ) {
            nftRepository
                .getCreatorSaleNft(creator)
                .onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.onCompletion {
                    _uiState.update { it.copy(isLoading = false) }
                }.collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> _uiEvent.emit(FrameDetailUiEvent.Error(response.errorMessage))
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    creatorFrames = response.data.filter { it.marketId != marketId },
                                )
                            }
                        }
                    }
                }
        }

        fun onClickedLikeNft(isLiked: Boolean) {
            _uiState.update { it.copy(isLiked = isLiked) }
            val marketId = uiState.value.frame.marketId
            viewModelScope.launch {
                if (isLiked) {
                    likeNft(marketId)
                } else {
                    disLikeNft(marketId)
                }
            }
        }

        private suspend fun likeNft(marketId: Long) {
            nftRepository.likeNft(marketId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _uiEvent.emit(FrameDetailUiEvent.Error(response.errorMessage))
                    }

                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(likedCount = response.data) }
                    }
                }
            }
        }

        private suspend fun disLikeNft(marketId: Long) {
            nftRepository.disLikeNft(marketId).collectLatest { response ->
                when (response) {
                    is ApiResponse.Error -> {
                        _uiEvent.emit(FrameDetailUiEvent.Error(response.errorMessage))
                    }

                    is ApiResponse.Success -> {
                        _uiState.update { it.copy(likedCount = response.data) }
                    }
                }
            }
        }

        fun purchaseNft(
            txHash: String,
            marketId: Long,
        ) {
            viewModelScope.launch {
                nftRepository
                    .purchaseNft(
                        request =
                            PurchaseNftRequest(
                                txHash = txHash,
                                marketId = marketId,
                            ),
                    ).onStart {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(FrameDetailUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> _uiEvent.emit(FrameDetailUiEvent.Success)
                        }
                    }
            }
        }
    }
