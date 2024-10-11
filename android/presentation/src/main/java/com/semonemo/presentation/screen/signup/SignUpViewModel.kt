package com.semonemo.presentation.screen.signup

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.request.SignUpRequest
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onCompletion
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class SignUpViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val savedStateHandle: SavedStateHandle,
    ) : ViewModel() {
        private val walletAddress = savedStateHandle.get<String>("walletAddress") ?: ""
        private val _uiState = MutableStateFlow(SignUpUiState())
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<SignUpUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        fun updateNickname(nickname: String) {
            _uiState.update {
                it.copy(
                    nickname = nickname,
                )
            }
        }

        fun updatePassword(password: String) {
            _uiState.update {
                it.copy(
                    password = password,
                )
            }
        }

        fun updateProfileImage(profileImage: File) {
            _uiState.update {
                it.copy(
                    profileImage = profileImage,
                )
            }
        }

        fun signUp() {
            val profileImage = uiState.value.profileImage
            viewModelScope.launch {
                validate(uiState.value.nickname).collectLatest { isValidNickname ->
                    if (isValidNickname && profileImage != null) {
                        authRepository
                            .signUp(
                                data =
                                    SignUpRequest(
                                        address = walletAddress,
                                        password = uiState.value.password,
                                        nickname = uiState.value.nickname,
                                    ),
                                profileImage = profileImage,
                            ).onStart {
                                _uiState.update { it.copy(isLoading = true) }
                            }.onCompletion {
                                _uiState.update { it.copy(isLoading = false) }
                            }.collectLatest { response ->
                                when (response) {
                                    is ApiResponse.Error -> {
                                        _uiEvent.emit(
                                            SignUpUiEvent.Error(
                                                code = response.errorCode,
                                                message = response.errorMessage,
                                            ),
                                        )
                                    }

                                    is ApiResponse.Success -> {
                                        _uiEvent.emit(SignUpUiEvent.SignUpSuccess)
                                    }
                                }
                            }
                    }
                }
            }
        }

        private suspend fun validate(nickname: String): Flow<Boolean> =
            authRepository.validateNickname(nickname).map { response ->
                when (response) {
                    is ApiResponse.Success -> true

                    is ApiResponse.Error -> {
                        _uiEvent.emit(
                            SignUpUiEvent.Error(
                                code = response.errorCode,
                                message = response.errorMessage,
                            ),
                        )
                        false
                    }
                }
            }
    }
