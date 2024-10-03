package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.request.PublishNftRequest
import kotlinx.coroutines.flow.Flow

interface NftRepository {
    suspend fun publishNft(request: PublishNftRequest): Flow<ApiResponse<Nft>>

    suspend fun getUserNft(userId: Long): Flow<ApiResponse<List<MyFrame>>>
}
