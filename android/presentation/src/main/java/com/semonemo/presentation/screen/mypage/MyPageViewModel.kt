package com.semonemo.presentation.screen.mypage

import android.util.Log
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.repository.UserRepository
import com.semonemo.domain.request.EditUserRequest
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class MyPageViewModel
    @Inject
    constructor(
        private val userRepository: UserRepository,
        private val nftRepository: NftRepository,
        private val assetRepository: AssetRepository,
        private val authDataSource: AuthDataSource,
        private val savedStateHandle: SavedStateHandle,
    ) : BaseViewModel() {
        private val _uiState = MutableStateFlow<MyPageUiState>(MyPageUiState.Loading)
        val uiState = _uiState.asStateFlow()
        private val _uiEvent = MutableSharedFlow<MyPageUiEvent>()
        val uiEvent = _uiEvent.asSharedFlow()
        private val userId = savedStateHandle.get<Long>("userId") ?: -1

        init {
            if (userId == -1L) { // 마이페이지
                loadUserInfo()
                loadMyComponents()
            } else { // 타사용자 페이지
                loadOtherUserInfo()
            }
        }

        fun loadOtherUserInfo() {
            viewModelScope.launch {
                combine(
                    userRepository.loadOtherUserInfo(userId),
                    userRepository.loadFollowing(userId),
                    userRepository.loadFollowers(userId),
                    userRepository.isFollow(userId),
                ) { userInfo, followingInfo, followerInfo, isFollow ->
                    var currentState = MyPageUiState.Success()

                    currentState =
                        when (userInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(userInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    userId = userInfo.data.userId,
                                    profileImageUrl = userInfo.data.profileImage,
                                    nickname = userInfo.data.nickname,
                                )
                            }
                        }

                    currentState =
                        when (followingInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(followingInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    following = followingInfo.data,
                                )
                            }
                        }

                    currentState =
                        when (followerInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(followerInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    follower = followerInfo.data,
                                )
                            }
                        }

                    currentState =
                        when (isFollow) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(isFollow.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    isFollow = isFollow.data,
                                )
                            }
                        }
                    currentState
                }.collectLatest { updatedUiState ->
                    _uiState.value = updatedUiState
                }
            }
        }

        private fun loadUserInfo() {
            viewModelScope.launch {
                combine(
                    userRepository.loadUserInfo(),
                    userRepository.loadFollowing(userId),
                    userRepository.loadFollowers(userId),
                ) { userInfo, followingInfo, followerInfo ->

                    Log.d("nakyung", "userinfo: " + userInfo.toString())
                    Log.d("nakyung", "followingInfo: " + followingInfo.toString())
                    Log.d("nakyung", "followerInfo: " + followerInfo.toString())

                    var currentState = MyPageUiState.Success()
                    currentState =
                        when (userInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(userInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    userId = userInfo.data.userId,
                                    profileImageUrl = userInfo.data.profileImage,
                                    nickname = userInfo.data.nickname,
                                )
                            }
                        }
                    currentState =
                        when (followingInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(followingInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    following = followingInfo.data,
                                )
                            }
                        }
                    currentState =
                        when (followerInfo) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(followerInfo.errorMessage))
                                currentState
                            }

                            is ApiResponse.Success -> {
                                currentState.copy(
                                    follower = followerInfo.data,
                                )
                            }
                        }
                    currentState
                }.collectLatest { updatedUiState ->
                    val state = _uiState.value
                    if (state is MyPageUiState.Loading) {
                        _uiState.value = updatedUiState
                    } else if (state is MyPageUiState.Success) {
                        _uiState.value =
                            state.copy(
                                userId = updatedUiState.userId,
                                profileImageUrl = updatedUiState.profileImageUrl,
                                nickname = updatedUiState.nickname,
                                following = updatedUiState.following,
                                follower = updatedUiState.follower,
                            )
                    }
                }
            }
        }

        private fun loadMyComponents() {
            viewModelScope.launch {
                authDataSource.getUserId()?.let { userId ->
                    combine(
                        nftRepository.getUserNft(userId.toLong()),
                        nftRepository.getSellNft(userId.toLong()),
                        assetRepository.getMyAssets(null),
                    ) { nftList, sellNftList, assetList ->

                        Log.d("nakyung", "nftList: " + nftList.toString())
                        Log.d("nakyung", "sellNftList: " + sellNftList.toString())
                        Log.d("nakyung", "assetList: " + assetList.toString())

                        var currentState = MyPageUiState.Success()

                        currentState =
                            when (nftList) {
                                is ApiResponse.Error -> {
                                    _uiEvent.emit(MyPageUiEvent.Error(nftList.errorMessage))
                                    currentState
                                }

                                is ApiResponse.Success -> {
                                    currentState.copy(
                                        frameList = nftList.data,
                                    )
                                }
                            }

                        currentState =
                            when (sellNftList) {
                                is ApiResponse.Error -> {
                                    _uiEvent.emit(MyPageUiEvent.Error(sellNftList.errorMessage))
                                    currentState
                                }

                                is ApiResponse.Success -> {
                                    currentState.copy(
                                        sellFrameList = sellNftList.data,
                                    )
                                }
                            }
                        currentState =
                            when (assetList) {
                                is ApiResponse.Error -> {
                                    _uiEvent.emit(MyPageUiEvent.Error(assetList.errorMessage))
                                    currentState
                                }

                                is ApiResponse.Success -> {
                                    currentState.copy(
                                        assetList = assetList.data,
                                    )
                                }
                            }
                        currentState
                    }.collectLatest { updatedUiState ->
                        val state = _uiState.value
                        if (state is MyPageUiState.Loading) {
                            _uiState.value = updatedUiState
                        } else if (state is MyPageUiState.Success) {
                            _uiState.value =
                                state.copy(
                                    frameList = updatedUiState.frameList,
                                    sellFrameList = updatedUiState.sellFrameList,
                                    assetList = updatedUiState.assetList,
                                )
                        }
                    }
                }
            }
        }

        fun updateProfileImage(
            image: File,
            imageUri: String,
        ) {
            val state = uiState.value
            if (state !is MyPageUiState.Success) {
                return
            }
            viewModelScope.launch {
                userRepository
                    .edit(profileImage = image, request = EditUserRequest(state.nickname))
                    .collectLatest { response ->
                        when (response) {
                            is ApiResponse.Error -> {
                                _uiEvent.emit(MyPageUiEvent.Error(response.errorMessage))
                            }

                            is ApiResponse.Success -> {
                                _uiState.value =
                                    state.copy(
                                        profileImageUrl = imageUri,
                                    )
                            }
                        }
                    }
            }
        }

        fun followUser(userId: Long) {
            val state = uiState.value
            if (state !is MyPageUiState.Success) {
                return
            }
            viewModelScope.launch {
                userRepository.followUser(userId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(MyPageUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            _uiState.value =
                                state.copy(
                                    isFollow = true,
                                )
                            _uiEvent.emit(MyPageUiEvent.Subscribe)
                        }
                    }
                }
            }
        }

        fun unfollowUser(userId: Long) {
            val state = uiState.value
            if (state !is MyPageUiState.Success) {
                return
            }
            viewModelScope.launch {
                userRepository.unfollowUser(userId).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _uiEvent.emit(MyPageUiEvent.Error(response.errorMessage))
                        }

                        is ApiResponse.Success -> {
                            _uiState.value =
                                state.copy(
                                    isFollow = false,
                                )
                            _uiEvent.emit(MyPageUiEvent.Subscribe)
                        }
                    }
                }
            }
        }
    }
