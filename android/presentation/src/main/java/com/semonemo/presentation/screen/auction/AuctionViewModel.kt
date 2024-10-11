package com.semonemo.presentation.screen.auction

import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.repository.AuctionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

private const val TAG = "AuctionViewModel"

@HiltViewModel
class AuctionViewModel
    @Inject
    constructor(
        private val auctionRepository: AuctionRepository,
    ) : ViewModel() {
        private val _uiEvent = MutableSharedFlow<AuctionUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()
        private val _uiState = MutableStateFlow(AuctionUiState())
        val uiState = _uiState.asStateFlow()
        var shortAuctionList = mutableStateOf<List<Auction>>(listOf())
            private set

        init {
//            loadShortAuction()
        }

        fun loadShortAuction() {
            viewModelScope.launch {
                auctionRepository.getAllAuction().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(AuctionUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            Log.d(TAG, "loadShortAuction: ${response.data}")
                            val allAuctionList = response.data
                            _uiState.update { it ->
                                it.copy(
                                    readyAuctionList = allAuctionList.filter { AuctionStatus.valueOf(it.status) == AuctionStatus.READY },
                                    progressAuctionList =
                                        allAuctionList.filter {
                                            AuctionStatus.valueOf(
                                                it.status,
                                            ) == AuctionStatus.PROGRESS
                                        },
                                    endAuctionList = allAuctionList.filter { AuctionStatus.valueOf(it.status) == AuctionStatus.END },
                                    cancelAuctionList = allAuctionList.filter { AuctionStatus.valueOf(it.status) == AuctionStatus.CANCEL },
                                )
                            }
                        }
                    }
                }
            }
        }
    }
