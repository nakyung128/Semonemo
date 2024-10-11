package com.semonemo.presentation.screen.frame

import android.graphics.Bitmap
import android.util.Log
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.toMutableStateList
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Asset
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.IpfsRepository
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.request.PublishNftRequest
import com.semonemo.domain.request.UploadFrameRequest
import com.semonemo.presentation.base.BaseViewModel
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
import java.io.File
import javax.inject.Inject

@HiltViewModel
class FrameViewModel
    @Inject
    constructor(
        private val ipfsRepository: IpfsRepository,
        private val nftRepository: NftRepository,
        private val assetRepository: AssetRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(FrameUiState())
        val uiState = _uiState.asStateFlow()
        var frame = mutableStateOf<Bitmap?>(null)
            private set
        private val _uiEvent = MutableSharedFlow<FrameUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()
        var assets = mutableStateOf<List<Asset>>(listOf())
            private set
        private val imageIpfsHash = mutableStateOf("")

        fun loadMyAssets() {
            viewModelScope.launch {
                assetRepository.getMyAssets(null).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> _uiEvent.emit(FrameUiEvent.Error(response.errorMessage))
                        is ApiResponse.Success -> assets.value = response.data
                    }
                }
            }
        }

        fun updateFrame(
            bitmap: Bitmap,
            frameType: FrameType,
        ) {
            _uiState.update { it.copy(bitmap = bitmap, frameType = frameType) }
        }

        fun uploadImage(file: File) {
            viewModelScope.launch {
                ipfsRepository
                    .uploadImage(file)
                    .onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion {
                        file.deleteOnExit()
                        _uiState.update { it.copy(isLoading = false) }
                    }.collectLatest { response ->
                        Log.d("jaehan", "uploadImage : $response")
                        when (response) {
                            is ApiResponse.Success -> {
                                imageIpfsHash.value = response.data.hash
                                uploadFrame(response.data.hash)
                            }

                            is ApiResponse.Error -> {
                                _uiEvent.emit(FrameUiEvent.Error(response.errorMessage))
                            }
                        }
                    }
            }
        }

        private suspend fun uploadFrame(imageHash: String) {
            ipfsRepository
                .uploadFrame(
                    request =
                        UploadFrameRequest(
                            title = uiState.value.title,
                            content = uiState.value.content,
                            image = imageHash,
                        ),
                ).onStart {
                    _uiState.update { it.copy(isLoading = true) }
                }.onCompletion {
                    _uiState.update { it.copy(isLoading = false) }
                }.collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(FrameUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            _uiEvent.emit(FrameUiEvent.UploadFinish(response.data.hash))
                        }
                    }
                }
        }

        fun updateTitle(title: String) {
            _uiState.update { it.copy(title = title) }
        }

        fun updateContent(content: String) {
            _uiState.update { it.copy(content = content) }
        }

        fun addTag(tag: String) {
            _uiState.update {
                val newList =
                    it.tags.toMutableStateList().apply {
                        add(tag)
                    }
                it.copy(
                    tags = newList,
                )
            }
        }

        fun deleteTag(tag: String) {
            _uiState.update {
                val newList =
                    it.tags.filter { t ->
                        t != tag
                    }
                it.copy(
                    tags = newList,
                )
            }
        }

        fun publishNft(
            imageHash: String,
            ipfsHash: String,
        ) {
            viewModelScope.launch {
                nftRepository
                    .publishNft(
                        PublishNftRequest(
                            txHash = imageHash,
                            tags = uiState.value.tags,
                            frameType = uiState.value.frameType.idx,
                        ),
                    ).onStart {
                        _uiState.update { it.copy(isLoading = true) }
                    }.onCompletion { _uiState.update { it.copy(isLoading = false) } }
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> _uiEvent.emit(FrameUiEvent.Error(response.errorMessage))
                            is ApiResponse.Success -> pin(ipfsHash)
                        }
                    }
            }
        }

        private suspend fun pin(ipfsHash: String) {
            combine(
                ipfsRepository.pin(ipfsHash),
                ipfsRepository.pin(imageIpfsHash.value),
            ) { pin1, pin2 ->
                if (pin1 is ApiResponse.Error) {
                    _uiEvent.emit(FrameUiEvent.Error(pin1.errorMessage))
                    return@combine
                }

                // pin2 처리
                if (pin2 is ApiResponse.Error) {
                    _uiEvent.emit(FrameUiEvent.Error(pin2.errorMessage))
                    return@combine
                }
                if (pin1 is ApiResponse.Success && pin2 is ApiResponse.Success) {
                    _uiEvent.emit(FrameUiEvent.NavigateMoment) // 성공 시 원하는 동작 수행
                }
            }.onStart {
                _uiState.update { it.copy(isLoading = true) }
            }.onCompletion {
                _uiState.update { it.copy(isLoading = false) }
            }.collectLatest { it ->
                Log.d("jaehan", "$it")
            }
        }
    }
