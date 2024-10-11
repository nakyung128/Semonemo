package com.semonemo.presentation.screen.auction.register

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.AuctionRegisterRequest
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.repository.AuctionRepository
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
class AuctionRegisterViewModel
    @Inject
    constructor(
        private val nftRepository: NftRepository,
        private val authDataSource: AuthDataSource,
        private val auctionRepository: AuctionRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(AuctionRegisterUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<AuctionRegisterUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            loadMyFrames()
        }

        private fun loadMyFrames() {
            viewModelScope.launch {
                authDataSource.getUserId()?.let { userId ->
                    nftRepository.getUserNft(userId.toLong()).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(AuctionRegisterUiEvent.Error(response.errorMessage))
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
        }

        fun selectFrame(frame: MyFrame) {
            _uiState.update { it.copy(selectFrame = frame) }
        }

        fun auctionRegisterFrame(
            price: Long,
            hash: String,
        ) {
            val selectFrame = _uiState.value.selectFrame
            if (selectFrame == null) {
                viewModelScope.launch {
                    _uiEvent.emit(AuctionRegisterUiEvent.Error("경매에 등록할 프레임을 선택해주세요!"))
                }
                return
            }

            viewModelScope.launch {
                auctionRepository
                    .registerAuction(
                        auction =
                            AuctionRegisterRequest(
                                nftId = selectFrame.nftId,
                                startPrice = price,
                                txHash = hash,
                            ),
                    ).onStart {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }.onCompletion {
                        _uiState.update {
                            it.copy(isLoading = false)
                        }
                    }.collectLatest { response ->
                        val event =
                            when (response) {
                                is ApiResponse.Error -> AuctionRegisterUiEvent.Error(response.errorMessage)
                                is ApiResponse.Success -> AuctionRegisterUiEvent.RegisterDone
                            }
                        _uiEvent.emit(event)
                    }
            }
            // 경매 로직 후 처리 예정
//            viewModelScope.launch {
//                nftRepository
//                    .sellRegisterNft(
//                        request =
//                            SellNftRequest(
//                                nftId = selectFrame.nftId,
//                                price = price,
//                                txHash = hash,
//                            ),
//                    ).onStart {
//                        _uiState.update {
//                            it.copy(isLoading = true)
//                        }
//                    }.onCompletion {
//                        _uiState.update {
//                            it.copy(isLoading = false)
//                        }
//                    }.collectLatest { response ->
//                        val event =
//                            when (response) {
//                                is ApiResponse.Error -> AuctionRegisterUiEvent.Error(response.errorMessage)
//                                is ApiResponse.Success -> AuctionRegisterUiEvent.RegisterDone
//                            }
//                        _uiEvent.emit(event)
//                    }
//            }
        }
    }
