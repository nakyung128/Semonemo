package com.semonemo.presentation.screen.search

import androidx.lifecycle.viewModelScope
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.SearchRepository
import com.semonemo.presentation.base.BaseViewModel
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SearchViewModel
    @Inject
    constructor(
        private val searchRepository: SearchRepository,
        private val authDataSource: AuthDataSource,
    ) : BaseViewModel() {
        private val _searchState = MutableStateFlow<SearchState>(SearchState.Loading)
        val searchState = _searchState.asStateFlow()

        init {
            loadHotSearch()
        }

        // 사람들이 많이 찾는 거 순위 불러오기
        fun loadHotSearch() {
            viewModelScope.launch {
                searchRepository.getHotKeywords().collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _searchState.value = SearchState.Error(response.errorMessage)
                        }

                        is ApiResponse.Success -> {
                            _searchState.value =
                                SearchState.Init(
                                    recentList = authDataSource.getCurrentKeyword(),
                                    hotList = response.data,
                                )
                        }
                    }
                }
            }
        }

        fun removeKeyword(keyword: String) {
            viewModelScope.launch {
                authDataSource.removeKeyword(keyword)
            }
        }

        fun addKeyword(keyword: String) {
            viewModelScope.launch {
                authDataSource.saveCurrentKeyword(keyword)
            }
        }

        // 유저 검색
        fun userSearch(keyword: String) {
            viewModelScope.launch {
                searchRepository.searchUser(keyword).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _searchState.value = SearchState.Error(response.errorMessage)
                        }

                        is ApiResponse.Success -> {
                            _searchState.value =
                                SearchState.Success(
                                    userList = response.data.content,
                                )
                        }
                    }
                }
            }
        }

        // 프레임 (NFT) 검색
        fun frameSearch(keyword: String) {
            viewModelScope.launch {
                searchRepository.searchFrame(keyword).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _searchState.value = SearchState.Error(response.errorMessage)
                        }

                        is ApiResponse.Success -> {
                            _searchState.value =
                                SearchState.Success(
                                    frameList = response.data.content,
                                )
                        }
                    }
                }
            }
        }

        // 에셋 검색
        fun assetSearch(keyword: String) {
            viewModelScope.launch {
                searchRepository.searchAsset(keyword).collectLatest { response ->
                    when (response) {
                        is ApiResponse.Error -> {
                            _searchState.value = SearchState.Error(response.errorMessage)
                        }

                        is ApiResponse.Success -> {
                            _searchState.value =
                                SearchState.Success(
                                    assetList = response.data.content,
                                )
                        }
                    }
                }
            }
        }
    }
