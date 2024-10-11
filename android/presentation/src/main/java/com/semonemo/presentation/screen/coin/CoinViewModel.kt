package com.semonemo.presentation.screen.coin

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.CoinRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
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
class CoinViewModel
    @Inject
    constructor(
        private val coinRepository: CoinRepository,
    ) : ViewModel() {
        private val _uiState = MutableStateFlow(CoinUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<CoinUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        init {
            getWeeklyCoin()
        }

        private fun getWeeklyCoin() {
            viewModelScope.launch {
                combine(
                    coinRepository.getCoinRate(),
                    coinRepository.getWeeklyCoin(),
                ) { coinRate, weeklyCoin ->

                    var currentUiState = CoinUiState()
                    currentUiState =
                        when (coinRate) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(CoinUiEvent.Error(coinRate.errorMessage))
                                currentUiState
                            }

                            is ApiResponse.Success ->
                                currentUiState.copy(
                                    coinPrice = coinRate.data.price,
                                    changed = coinRate.data.changed,
                                )
                        }

                    currentUiState =
                        when (weeklyCoin) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(CoinUiEvent.Error(weeklyCoin.errorMessage))
                                currentUiState
                            }

                            is ApiResponse.Success -> {
                                currentUiState.copy(
                                    weeklyCoin = weeklyCoin.data,
                                )
                            }
                        }
                    currentUiState
                }.onStart {
                    _uiState.update { it.copy(isLoading = true) }
                    delay(1000L)
                }.onCompletion {
                    _uiState.update { it.copy(isLoading = false) }
                }.collectLatest { updatedUiState ->
                    _uiState.value = updatedUiState
                }
            }
        }
    }
