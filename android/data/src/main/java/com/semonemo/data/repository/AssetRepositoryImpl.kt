package com.semonemo.data.repository

import com.semonemo.data.network.api.AssetApi
import com.semonemo.data.network.response.GetAssetsResponse
import com.semonemo.data.network.response.GetCreatorAssetsResponse
import com.semonemo.data.network.response.LikeResponse
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.data.util.toMultiPart
import com.semonemo.domain.model.AllSellAssets
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Asset
import com.semonemo.domain.model.CreateAiAsset
import com.semonemo.domain.model.CreateAsset
import com.semonemo.domain.model.SellAsset
import com.semonemo.domain.model.SellAssetDetail
import com.semonemo.domain.repository.AssetRepository
import com.semonemo.domain.request.PurchaseAssetRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.io.File
import javax.inject.Inject

class AssetRepositoryImpl
    @Inject
    constructor(
        private val api: AssetApi,
    ) : AssetRepository {
        override suspend fun registerAsset(image: File): Flow<ApiResponse<Unit>> =
            flow {
                val requestFile = image.toMultiPart()
                val response =
                    emitApiResponse(
                        apiResponse = { api.registerAsset(requestFile) },
                        default = CreateAiAsset(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(Unit))
                }
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

        override suspend fun getCreateAssets(userId: Long): Flow<ApiResponse<List<Asset>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getCreateAssets(userId) },
                        default = CreateAsset(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data.content))
                }
            }

        override suspend fun likeAsset(assetSellId: Long): Flow<ApiResponse<Long>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.likeAsset(assetSellId) },
                        default = LikeResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.likedCount))
                }
            }

        override suspend fun unlikeAsset(assetSellId: Long): Flow<ApiResponse<Long>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.unlikeAsset(assetSellId) },
                        default = LikeResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.likedCount))
                }
            }

        override suspend fun getSaleAssetDetail(assetSellId: Long): Flow<ApiResponse<SellAssetDetail>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.getSaleAssetDetail(assetSellId) },
                        default = SellAssetDetail(),
                    ),
                )
            }

        override suspend fun getLikeAssets(): Flow<ApiResponse<AllSellAssets>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getLikeAssets() },
                        default = AllSellAssets(),
                    )
                emit(response)
            }

        override suspend fun purchaseAsset(request: PurchaseAssetRequest): Flow<ApiResponse<Unit>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.purchaseAsset(request) },
                        default = Unit,
                    ),
                )
            }

        override suspend fun getCreatorAsset(userId: Long): Flow<ApiResponse<List<SellAssetDetail>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getCreatorAsset(userId) },
                        default = GetCreatorAssetsResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data =
                                    response.data.content.map {
                                        SellAssetDetail(
                                            assetSellId = it.assetSellId,
                                            assetId = it.assetId,
                                            creator = it.creator,
                                            createAt = it.createdAt,
                                            price = it.price,
                                            imageUrl = it.imageUrl,
                                            hits = it.hits,
                                            isLiked = it.isLiked,
                                            likeCount = it.likedCount,
                                        )
                                    },
                            ),
                        )
                }
            }

        override suspend fun getUnSaleAssets(): Flow<ApiResponse<List<Asset>>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.getUnSaleAssets() }, default = CreateAsset())
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data.content))
                }
            }
    }
