package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.User
import com.semonemo.domain.request.EditUserRequest
import kotlinx.coroutines.flow.Flow
import java.io.File

interface UserRepository {
    suspend fun edit(
        profileImage: File? = null,
        request: EditUserRequest,
    ): Flow<ApiResponse<Unit>>

    suspend fun loadUserInfo(): Flow<ApiResponse<User>>

    suspend fun delete(): Flow<ApiResponse<Unit>>

    suspend fun loadFollowing(userId: Long): Flow<ApiResponse<List<User>>>

    suspend fun loadFollowers(userId: Long): Flow<ApiResponse<List<User>>>

    suspend fun loadOtherUserInfo(userId: Long): Flow<ApiResponse<User>>

    suspend fun isFollow(userId: Long): Flow<ApiResponse<Boolean>>

    suspend fun followUser(userId: Long): Flow<ApiResponse<Unit>>

    suspend fun unfollowUser(userId: Long): Flow<ApiResponse<Unit>>
}
