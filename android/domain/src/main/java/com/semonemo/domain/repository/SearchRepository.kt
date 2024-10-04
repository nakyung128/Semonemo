package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.HotKeyword
import com.semonemo.domain.model.SearchAsset
import com.semonemo.domain.model.SearchFrame
import com.semonemo.domain.model.SearchUser
import kotlinx.coroutines.flow.Flow

interface SearchRepository {
    suspend fun searchUser(keyword: String): Flow<ApiResponse<SearchUser>>

    suspend fun searchAsset(keyword: String): Flow<ApiResponse<SearchAsset>>

    suspend fun searchFrame(keyword: String): Flow<ApiResponse<SearchFrame>>

    suspend fun getHotKeywords(): Flow<ApiResponse<List<HotKeyword>>>
}
