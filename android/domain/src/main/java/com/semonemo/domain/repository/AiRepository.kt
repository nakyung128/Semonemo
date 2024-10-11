package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.request.RemoveBgRequest
import com.semonemo.domain.request.makeAiAsset.MakeAiAssetRequest
import kotlinx.coroutines.flow.Flow

interface AiRepository {
    suspend fun makeAiAsset(request: MakeAiAssetRequest): Flow<ApiResponse<String>>

    suspend fun removeBg(request: RemoveBgRequest): Flow<ApiResponse<String>>
}
