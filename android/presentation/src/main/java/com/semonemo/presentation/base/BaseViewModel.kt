package com.semonemo.presentation.base

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
open class BaseViewModel
    @Inject
    constructor() : ViewModel() {
        private val _baseUiEvent = MutableSharedFlow<BaseUiEvent>()
        val baseUiEvent: SharedFlow<BaseUiEvent> = _baseUiEvent

        protected fun handleApiError(error: ApiResponse.Error) {
            when (error) {
                is ApiResponse.Error.TokenError -> {
                    viewModelScope.launch {
                        _baseUiEvent.emit(BaseUiEvent.Login)
                    }
                }

                else -> {
                    viewModelScope.launch {
                        _baseUiEvent.emit(
                            BaseUiEvent.Error(
                                code = error.errorCode,
                                message = error.errorMessage,
                            ),
                        )
                    }
                }
            }
        }
    }
