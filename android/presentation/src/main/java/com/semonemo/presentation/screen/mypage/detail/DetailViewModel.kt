package com.semonemo.presentation.screen.mypage.detail

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.NftRepository
import com.semonemo.presentation.base.BaseViewModel
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
class DetailViewModel
    @Inject
    constructor(
        private val nftRepository: NftRepository,
        private val savedStateHandle: SavedStateHandle,
        private val authDataSource: AuthDataSource,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(DetailUiState())
        val uiState = _uiState.asStateFlow()

        private val _uiEvent = MutableSharedFlow<DetailUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        private val id = savedStateHandle.get<Long>("marketId") ?: -1
        private val isSale = savedStateHandle.get<Boolean>("isSale") ?: false

        init {
            getNftDetail()
        }

        // NFT 공개, 비공개 전환
        fun openNft() {
            viewModelScope.launch {
                if (uiState.value.nftId != -1L) {
                    nftRepository.openNft(uiState.value.nftId).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(DetailUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                _uiEvent.emit(DetailUiEvent.OpenSuccess("공개 여부가 전환되었습니다."))
                                _uiState.update { currentState ->
                                    currentState.copy(isOpen = !currentState.isOpen)
                                }
                            }
                        }
                    }
                }
            }
        }

        // NFT 자세히 불러오기
        private fun getNftDetail() {
            viewModelScope.launch {
                if (id == -1L) {
                    _uiEvent.emit(DetailUiEvent.Error("잘못된 nft id 입니다."))
                    return@launch
                }
                if (!isSale) {
                    nftRepository.getNftDetail(id).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(DetailUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                val data = response.data
                                _uiState.value =
                                    DetailUiState(
                                        userId = authDataSource.getUserId()?.toLong() ?: -1L,
                                        ownerId = data.owner.userId,
                                        owner = data.owner.nickname,
                                        profileImg = data.owner.profileImage,
                                        tags = data.tags,
                                        isOpen = data.isOpen,
                                        isOnSale = data.isOnSale,
                                        title = data.nftInfo.data.title,
                                        content = data.nftInfo.data.content,
                                        image = data.nftInfo.data.image,
                                        marketId = data.nftInfo.tokenId,
                                        tokenId = data.nftInfo.tokenId,
                                        nftId = data.nftId,
                                    )
                            }
                        }
                    }
                } else {
                    nftRepository.getSaleNftDetail(marketId = id).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(DetailUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> {
                                val data = response.data
                                _uiState.value =
                                    DetailUiState(
                                        userId = authDataSource.getUserId()?.toLong() ?: -1L,
                                        ownerId = data.seller.userId,
                                        owner = data.seller.nickname,
                                        profileImg = data.seller.profileImage,
                                        tags = data.tags,
                                        isOpen = true,
                                        isOnSale = true,
                                        title = data.nftInfo.data.title,
                                        content = data.nftInfo.data.content,
                                        image = data.nftInfo.data.image,
                                        marketId = data.marketId,
                                        tokenId = data.nftInfo.tokenId,
                                        nftId = data.nftId,
                                    )
                            }
                        }
                    }
                }
            }
        }

        fun cancelSaleNft(
            txHash: String,
            marketId: Long,
        ) {
            viewModelScope.launch {
                nftRepository
                    .cancelSaleNft(
                        txHash = txHash,
                        marketId = marketId,
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(DetailUiEvent.Error(errorMessage = response.errorMessage))
                            is ApiResponse.Success -> {
                                _uiEvent.emit(DetailUiEvent.Error(errorMessage = "판매가 취소되었습니다."))
                                _uiState.update {
                                    it.copy(
                                        isOnSale = false,
                                        isOpen = true,
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
