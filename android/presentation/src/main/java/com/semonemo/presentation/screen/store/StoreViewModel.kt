package com.semonemo.presentation.screen.store

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.NftRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.update
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

        init {
            loadStoreInfo()
        }

        private fun loadStoreInfo() {
            viewModelScope.launch {
                nftRepository.getAllSaleNft().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {}
                        is ApiResponse.Success -> {
                            _uiState.update { it.copy(saleFrame = response.data) }
                        }
                    }
                }
            }
        }
    }
