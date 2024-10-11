package com.semonemo.presentation.screen.wallet

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.CoinRepository
import com.semonemo.domain.request.ExchangePayableRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WalletViewModel
    @Inject
    constructor(
        private val coinRepository: CoinRepository,
        private val authDataSource: AuthDataSource,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(WalletUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<WalletUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            loadUserInfo()
            loadCoinHistory()
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                _uiState.update {
                    it.copy(
                        userId = authDataSource.getUserId()?.toLong() ?: -1L,
                        nickname = authDataSource.getNickname() ?: "",
                    )
                }
            }
        }

        private fun loadCoinHistory() {
            viewModelScope.launch {
                combine(
                    coinRepository.getCoinHistory(),
                    coinRepository.getBalance(),
                    coinRepository.getCoinRate(),
                ) { historyInfo, coinInfo, rateInto ->
                    var currentState = WalletUiState()
                    currentState =
                        when (coinInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(WalletUiEvent.Error(coinInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    userCoin = coinInfo.data,
                                )
                            }
                        }

                    currentState =
                        when (historyInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(WalletUiEvent.Error(historyInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    coinHistory = historyInfo.data,
                                )
                            }
                        }
                    currentState =
                        when (rateInto) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(WalletUiEvent.Error(rateInto.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success ->
                                currentState.copy(
                                    coinPrice = rateInto.data.price,
                                    coinChanged = rateInto.data.changed,
                                )
                        }
                    currentState
                }.onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.onCompletion { _uiState.update { it.copy(isLoading = false) } }
                    .collectLatest { updateState ->
                        _uiState.update {
                            it.copy(
                                coinHistory = updateState.coinHistory,
                                userCoin = updateState.userCoin,
                                coinPrice = updateState.coinPrice,
                                coinChanged = updateState.coinChanged,
                            )
                        }
                    }
            }
        }

        fun exchangeCoinPayable(
            amount: Long,
            txHash: String,
        ) {
            viewModelScope.launch {
                coinRepository
                    .exchangeCoinPayable(
                        request =
                            ExchangePayableRequest(
                                txHash = txHash,
                                amount = amount,
                            ),
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion { _uiState.update { it.copy(isLoading = false) } }
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(WalletUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> loadCoinHistory()
                        }
                    }
            }
        }

        fun exchangePayableCoin(
            amount: Long,
            txHash: String,
        ) {
            viewModelScope.launch {
                coinRepository
                    .exchangePayableCoin(
                        request =
                            ExchangePayableRequest(
                                txHash = txHash,
                                amount = amount,
                            ),
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion { _uiState.update { it.copy(isLoading = false) } }
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(WalletUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> loadCoinHistory()
                        }
                    }
            }
        }

        fun buyCoin(amount: Long) {
            viewModelScope.launch {
                coinRepository
                    .buyCoin(amount)
                    .onStart {
                        _uiState.update {
                            it.copy(isLoading = true)
                        }
                    }.onCompletion {
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(WalletUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> {
                                _uiEvent.emit(WalletUiEvent.PaySuccess(message = "충전에 성공했습니다."))
                                _uiState.update {
                                    it.copy(
                                        userCoin = response.data,
                                    )
                                }
                            }
                        }
                    }
            }
        }
    }
