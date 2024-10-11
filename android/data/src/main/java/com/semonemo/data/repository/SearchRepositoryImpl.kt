package com.semonemo.data.repository

import com.semonemo.data.network.api.SearchApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.HotKeyword
import com.semonemo.domain.model.SearchAsset
import com.semonemo.domain.model.SearchFrame
import com.semonemo.domain.model.SearchUser
import com.semonemo.domain.repository.SearchRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class SearchRepositoryImpl
    @Inject
    constructor(
        private val api: SearchApi,
    ) : SearchRepository {
        override suspend fun searchUser(keyword: String): Flow<ApiResponse<SearchUser>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.searchUsers(keyword) },
                        default = SearchUser(),
                    )
                emit(response)
            }

        override suspend fun searchAsset(keyword: String): Flow<ApiResponse<SearchAsset>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.searchAsset(
                                keyword = keyword,
                                orderBy = "latest",
                            )
                        },
                        default = SearchAsset(),
                    )
                emit(response)
            }

        override suspend fun searchFrame(keyword: String): Flow<ApiResponse<SearchFrame>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.searchNft(keyword)
                        },
                        default = SearchFrame(),
                    )
                emit(response)
            }

        override suspend fun getHotKeywords(): Flow<ApiResponse<List<HotKeyword>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getHotKeywords() },
                        default = listOf(),
                    )
                emit(response)
            }
    }
