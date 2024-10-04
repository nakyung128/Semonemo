package com.semonemo.domain.repository

import com.semonemo.domain.model.AllSellAssets
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.CreateAsset
import com.semonemo.domain.model.SellAsset
import com.semonemo.domain.model.SellAssetDetail
import kotlinx.coroutines.flow.Flow
import java.io.File

interface AssetRepository {
    suspend fun registerAsset(image: File): Flow<ApiResponse<Asset>>

    suspend fun getMyAssets(cursorId: Long?): Flow<ApiResponse<List<Asset>>>

    suspend fun sellAsset(asset: SellAsset): Flow<ApiResponse<Unit>>

    suspend fun getAssetDetail(assetId: Long): Flow<ApiResponse<Asset>>

    suspend fun getSellAssetDetail(assetSellId: Long): Flow<ApiResponse<SellAssetDetail>>

    suspend fun getAllSellAssets(option: String): Flow<ApiResponse<AllSellAssets>>

    suspend fun getCreateAssets(userId: Long): Flow<ApiResponse<CreateAsset>>

    suspend fun likeAsset(assetSellId: Long): Flow<ApiResponse<Long>>

    suspend fun unlikeAsset(assetSellId: Long): Flow<ApiResponse<Long>>

    suspend fun getSaleAssetDetail(assetSellId: Long): Flow<ApiResponse<SellAssetDetail>>
}
