package com.semonemo.data.repository

import com.semonemo.data.network.api.AiApi
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.repository.AiRepository
import com.semonemo.domain.request.RemoveBgRequest
import com.semonemo.domain.request.makeAiAsset.MakeAiAssetRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class AiRepositoryImpl
    @Inject
    constructor(
        private val aiApi: AiApi,
    ) : AiRepository {
        override suspend fun makeAiAsset(request: MakeAiAssetRequest): Flow<ApiResponse<String>> =
            flow {
                runCatching {
                    aiApi.makeAiAsset(request)
                }.onSuccess { it ->
                    emit(ApiResponse.Success(it.images?.first() ?: ""))
                }.onFailure {
                    emit(ApiResponse.Error.UnknownError(errorMessage = it.message ?: ""))
                }
            }

        override suspend fun removeBg(request: RemoveBgRequest): Flow<ApiResponse<String>> =
            flow {
                runCatching {
                    aiApi.removeBg(request)
                }.onSuccess {
                    emit(ApiResponse.Success(it.image))
                }.onFailure {
                    emit(ApiResponse.Error.UnknownError(errorMessage = it.message ?: ""))
                }
            }
    }
