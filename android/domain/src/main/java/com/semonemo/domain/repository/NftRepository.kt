package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.request.PublishNftRequest
import com.semonemo.domain.request.SellNftRequest
import kotlinx.coroutines.flow.Flow

interface NftRepository {
    suspend fun publishNft(request: PublishNftRequest): Flow<ApiResponse<Nft>>

    suspend fun getUserNft(userId: Long): Flow<ApiResponse<List<MyFrame>>>

    suspend fun getAvailableNft(type: Int): Flow<ApiResponse<List<MyFrame>>>

    suspend fun sellRegisterNft(request: SellNftRequest): Flow<ApiResponse<FrameDetail>>

    suspend fun getAllSaleNft(): Flow<ApiResponse<List<FrameDetail>>>

    suspend fun getFrameDetail(marketId: Long): Flow<ApiResponse<FrameDetail>>

    suspend fun likeNft(marketId: Long): Flow<ApiResponse<Long>>

    suspend fun disLikeNft(marketId: Long): Flow<ApiResponse<Long>>
}
