package com.semonemo.data.repository

import com.semonemo.data.network.api.AssetApi
import com.semonemo.data.network.response.GetAssetsResponse
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.data.util.toMultiPart
import com.semonemo.domain.model.AllSellAssets
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.CreateAsset
import com.semonemo.domain.model.SellAsset
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.domain.repository.AssetRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class AssetRepositoryImpl
    @Inject
    constructor(
        private val api: AssetApi,
    ) : AssetRepository {
        override suspend fun registerAsset(image: File): Flow<ApiResponse<Asset>> =
            flow {
                val requestFile = image.toMultiPart()
                emit(
                    emitApiResponse(
                        apiResponse = { api.registerAsset(requestFile) },
                        default = Asset(),
                    ),
                )
            }

        override suspend fun getMyAssets(cursorId: Long?): Flow<ApiResponse<List<Asset>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getMyAssets(cursorId) },
                        default = GetAssetsResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data.content))
                }
            }

        override suspend fun sellAsset(asset: SellAsset): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.sellAsset(asset) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun getAssetDetail(assetId: Long): Flow<ApiResponse<Asset>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAssetDetail(assetId) },
                        default = Asset(),
                    )
                emit(response)
            }

        override suspend fun getSellAssetDetail(assetSellId: Long): Flow<ApiResponse<SellAssetDetail>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getSellAssetDetail(assetSellId) },
                        default = SellAssetDetail(),
                    )
                emit(response)
            }

        override suspend fun getAllSellAssets(option: String): Flow<ApiResponse<AllSellAssets>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAllSellAssets(option) },
                        default = AllSellAssets(),
                    )
                emit(response)
            }

        override suspend fun getCreateAssets(userId: Long): Flow<ApiResponse<CreateAsset>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getCreateAssets(userId) },
                        default = CreateAsset(),
                    )
                emit(response)
            }

        override suspend fun likeAsset(assetSellId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.likeAsset(assetSellId) },
                        default = Unit,
                    )
                emit(response)
            }

        override suspend fun unlikeAsset(assetSellId: Long): Flow<ApiResponse<Unit>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.unlikeAsset(assetSellId) },
                        default = Unit,
                    )
                emit(response)
            }
    }
