package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.NftData
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.request.PublishNftRequest
import com.semonemo.domain.request.PurchaseNftRequest
import com.semonemo.domain.request.SellNftRequest
import kotlinx.coroutines.flow.Flow

interface NftRepository {
    suspend fun publishNft(request: PublishNftRequest): Flow<ApiResponse<Nft>>

    suspend fun getUserNft(userId: Long): Flow<ApiResponse<List<MyFrame>>>

    suspend fun getSellNft(userId: Long): Flow<ApiResponse<List<FrameDetail>>>

    suspend fun getAvailableNft(type: Int): Flow<ApiResponse<List<MyFrame>>>

    suspend fun sellRegisterNft(request: SellNftRequest): Flow<ApiResponse<FrameDetail>>

    suspend fun getAllSaleNft(orderBy: String): Flow<ApiResponse<List<FrameDetail>>>

    suspend fun getSaleNftDetail(marketId: Long): Flow<ApiResponse<FrameDetail>>

    suspend fun likeNft(marketId: Long): Flow<ApiResponse<Long>>

    suspend fun disLikeNft(marketId: Long): Flow<ApiResponse<Long>>

    suspend fun openNft(nftId: Long): Flow<ApiResponse<Unit>>

    suspend fun getNftDetail(nftId: Long): Flow<ApiResponse<MyFrame>>

    suspend fun getSaleLikeNft(): Flow<ApiResponse<List<FrameDetail>>>

    suspend fun purchaseNft(request: PurchaseNftRequest): Flow<ApiResponse<MyFrame>>

    suspend fun cancelSaleNft(
        txHash: String,
        marketId: Long,
    ): Flow<ApiResponse<Unit>>

    suspend fun getCreatorSaleNft(creator: Long): Flow<ApiResponse<List<FrameDetail>>>

    suspend fun getHotNft(): Flow<ApiResponse<List<FrameDetail>>>
}
