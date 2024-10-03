package com.semonemo.presentation.screen.store.assetSale

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.SellAsset
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AssetSaleViewModel
    @Inject
    constructor(
        private val assetRepository: AssetRepository,
        private val authDataSource: AuthDataSource,
    ) : BaseViewModel() {
        private val _assetState = MutableStateFlow<AssetSaleState>(AssetSaleState.Init)
        val assetState = _assetState.asStateFlow()

        init {
            getCreateAssets()
        }

        private fun getCreateAssets() {
            viewModelScope.launch {
                authDataSource.getUserId()?.let { userId ->
                    assetRepository.getCreateAssets(userId.toLong()).collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _assetState.value = AssetSaleState.Error(response.errorMessage)
                            }

                            is ApiResponse.Success -> {
                                _assetState.value =
                                    AssetSaleState.LoadSuccess(
                                        assets = response.data.content,
                                    )
                            }
                        }
                    }
                }
            }
        }

        fun sellAsset(asset: SellAsset) {
            viewModelScope.launch {
                assetRepository.sellAsset(asset).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _assetState.value = AssetSaleState.Error(response.errorMessage)
                        }

                        is ApiResponse.Success -> {
                            _assetState.value = AssetSaleState.SellSuccess
                        }
                    }
                }
            }
        }
    }
