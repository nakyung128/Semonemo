package com.semonemo.data.repository

import com.google.gson.Gson
import com.semonemo.data.network.api.UserApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.data.util.toMultiPart
import com.semonemo.domain.datasource.AuthDataSource
import com.semonemo.domain.datasource.TokenDataSource
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.User
import com.semonemo.domain.repository.UserRepository
import com.semonemo.domain.request.EditUserRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.RequestBody.Companion.toRequestBody
import java.io.File
import javax.inject.Inject

class UserRepositoryImpl
    @Inject
    constructor(
        private val api: UserApi,
        private val authDataSource: AuthDataSource,
        private val tokenDataSource: TokenDataSource,
    ) : UserRepository {
        override suspend fun edit(
            profileImage: File?,
            request: EditUserRequest,
        ): Flow<ApiResponse<Unit>> =
            flow {
                val image = profileImage.toMultiPart()
                val requestBody =
                    Gson().toJson(request).toRequestBody("application/json".toMediaTypeOrNull())
                val response =
                    emitApiResponse(apiResponse = {
                        api.edit(
                            image = image,
                            data = requestBody,
                        )
                    }, default = Unit)
                if (response is ApiResponse.Success) {
                    authDataSource.saveNickname(request.nickname)
                    profileImage?.let {
                        authDataSource.saveProfileImage(it.path)
                    }
                }
                emit(response)
            }

        override suspend fun loadUserInfo(): Flow<ApiResponse<User>> =
            flow {
                val response = emitApiResponse(apiResponse = { api.loadMyInfo() }, default = User())
                if (response is ApiResponse.Success) {
                    response.data.apply {
                        authDataSource.saveUserId(userId)
                        authDataSource.saveNickname(nickname)
                        authDataSource.saveWalletAddress(address)
                        authDataSource.saveProfileImage(profileImage)
                    }
                }
                emit(response)
            }

        override suspend fun delete(): Flow<ApiResponse<Unit>> =
            flow {
                val response = emitApiResponse(apiResponse = { api.delete() }, default = Unit)
                if (response is ApiResponse.Success) {
                    tokenDataSource.deleteJwtToken()
                    authDataSource.deleteAuthData()
                }
                emit(response)
            }

        override suspend fun loadFollowing(userId: Long): Flow<ApiResponse<List<User>>> =
            flow {
                val response =
                    if (userId == -1L) { // 마이페이지
                        emitApiResponse(apiResponse = {
                            api.loadFollowing(
                                authDataSource.getUserId()?.toLong() ?: -1,
                            )
                        }, default = listOf())
                    } else { // 타사용자
                        emitApiResponse(apiResponse = { api.loadFollowing(userId) }, default = listOf())
                    }
                emit(response)
            }

        override suspend fun loadFollowers(userId: Long): Flow<ApiResponse<List<User>>> =
            flow {
                val response =
                    if (userId == -1L) { // 마이페이지
                        emitApiResponse(apiResponse = {
                            api.loadFollowers(
                                authDataSource.getUserId()?.toLong() ?: -1,
                            )
                        }, default = listOf())
                    } else { // 타사용자
                        emitApiResponse(apiResponse = { api.loadFollowers(userId) }, default = listOf())
                    }
                emit(response)
            }

        override suspend fun loadOtherUserInfo(userId: Long): Flow<ApiResponse<User>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = {
                        api.loadOtherUserInfo(userId)
                    }, default = User())
                emit(response)
            }

        override suspend fun isFollow(userId: Long): Flow<ApiResponse<Boolean>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = {
                        api.isFollow(userId)
                    }, default = false)
                emit(response)
            }

        override suspend fun followUser(userId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.followUser(userId)
                        },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun unfollowUser(userId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = {
                            api.unfollowUser(userId)
                        },
                        default = Unit,
                    )
                emit(response)
            }
    }
