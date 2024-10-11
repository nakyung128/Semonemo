package com.semonemo.presentation.screen.login

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.TokenDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AuthRepository
import com.semonemo.domain.request.LoginRequest
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LoginViewModel
    @Inject
    constructor(
        private val authRepository: AuthRepository,
        private val tokenDataSource: TokenDataSource,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow<LoginUiState>(LoginUiState.Init)
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<LoginUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()

        private fun authLogin() {
            viewModelScope.launch {
                val jwtToken = tokenDataSource.getJwtToken()
                if (jwtToken.first.isEmpty() && jwtToken.second.isEmpty()) {
                    return@launch
                }
                authRepository.login().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error ->
                            _uiEvent.emit(
                                LoginUiEvent.Error(
                                    errorCode = response.errorCode,
                                    errorMessage = response.errorMessage,
                                ),
                            )

                        is ApiResponse.Success -> if (response.data) _uiEvent.emit(LoginUiEvent.AutoLogin)
                    }
                }
            }
        }

        fun existUser(address: String) {
            viewModelScope.launch {
                authRepository.exists(address).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            if (response.data) {
                                _uiState.value =
                                    LoginUiState.Loading(
                                        isWalletLoading = true,
                                        walletAddress = address,
                                    )
                                authLogin()
                            } else {
                                _uiEvent.emit(LoginUiEvent.RequiredRegister(address))
                            }
                        }

                        is ApiResponse.Error -> {
                            _uiEvent.emit(
                                LoginUiEvent.Error(
                                    errorCode = response.errorCode,
                                    errorMessage = response.errorMessage,
                                ),
                            )
                        }
                    }
                }
            }
        }

        fun login(password: String) {
            val state = uiState.value
            viewModelScope.launch {
                if (state is LoginUiState.Loading) {
                    authRepository
                        .login(
                            LoginRequest(
                                address = state.walletAddress,
                                password = password,
                            ),
                        ).collectLatest { response ->
                            when (response) {
                                is ApiResponse.Error -> {
                                    _uiEvent.emit(
                                        LoginUiEvent.Error(
                                            errorCode = response.errorCode,
                                            errorMessage = response.errorMessage,
                                        ),
                                    )
                                }

                                is ApiResponse.Success -> {
                                    _uiEvent.emit(LoginUiEvent.LoginSuccess)
                                }
                            }
                        }
                }
            }
        }
    }
