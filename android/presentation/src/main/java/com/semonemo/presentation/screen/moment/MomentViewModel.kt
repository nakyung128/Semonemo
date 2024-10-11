package com.semonemo.presentation.screen.moment

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.UserRepository
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.BufferOverflow
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.scan
import kotlinx.coroutines.flow.shareIn
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.flow.transformLatest
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MomentViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow(MomentUiState())
        val uiState = _uiState.asStateFlow()
        private val BACK_PRESSED_DURATION = 2000L // 2초

        // 뒤로 가기 이벤트를 처리하기 위한 SharedFlow
        val backPressEvent =
            MutableSharedFlow<Unit>(
                replay = 0,
                extraBufferCapacity = 1,
                onBufferOverflow = BufferOverflow.DROP_OLDEST,
            )

        // 스낵바 표시 여부를 제어하는 StateFlow
        val isShowBackPressSnackBar =
            backPressEvent
                .transformLatest {
                    emit(true)
                    delay(BACK_PRESSED_DURATION)
                    emit(false)
                }.stateIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    initialValue = false,
                )

        val finishEvent =
            backPressEvent
                .scan(emptyList<Long>()) { acc, _ ->
                    val currentTime = System.currentTimeMillis()
                    (acc + currentTime).takeLast(2)
                }.filter { timestamps ->
                    timestamps.size >= 2 && (timestamps[1] - timestamps[0] < BACK_PRESSED_DURATION)
                }.shareIn(
                    scope = viewModelScope,
                    started = SharingStarted.Lazily,
                    replay = 0,
                )

        // Back 버튼이 눌렸을 때 호출되는 함수
        fun onBackPressed() {
            backPressEvent.tryEmit(Unit)
        }

        init {
            loadUserInfo()
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                userRepository.loadUserInfo().collect { response ->
                    when (response) {
                        is ApiResponse.Success -> {
                            _uiState.update {
                                it.copy(
                                    userId = response.data.userId,
                                    nickname = response.data.nickname,
                                )
                            }
                        }

                        else -> {}
                    }
                }
            }
        }
    }
