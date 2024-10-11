package com.semonemo.data.repository

import com.semonemo.data.network.api.NftApi
import com.semonemo.data.network.response.LikeResponse
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.SearchFrame
import com.semonemo.domain.model.myFrame.GetMyFrameResponse
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.request.PublishNftRequest
import com.semonemo.domain.request.PurchaseNftRequest
import com.semonemo.domain.request.SellNftRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class NftRepositoryImpl
    @Inject
    constructor(
        private val api: NftApi,
    ) : NftRepository {
        override suspend fun publishNft(request: PublishNftRequest): Flow<ApiResponse<Nft>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.publishNft(request) }, default = Nft())
                emit(response)
            }

        override suspend fun getUserNft(userId: Long): Flow<ApiResponse<List<MyFrame>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getUserNft(userId) },
                        default = GetMyFrameResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.content))
                }
            }

        override suspend fun getSellNft(userId: Long): Flow<ApiResponse<List<FrameDetail>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getSellNft(userId) },
                        default = SearchFrame(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.content))
                }
            }

        override suspend fun getAvailableNft(type: Int): Flow<ApiResponse<List<MyFrame>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAvailableNft(type) },
                        default = GetMyFrameResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.content))
                }
            }

        override suspend fun sellRegisterNft(request: SellNftRequest): Flow<ApiResponse<FrameDetail>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.sellRegisterNft(request) },
                        default = FrameDetail(),
                    ),
                )
            }

        override suspend fun getAllSaleNft(orderBy: String): Flow<ApiResponse<List<FrameDetail>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAllSaleNft(orderBy = orderBy) },
                        default = SearchFrame(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.content))
                }
            }

        override suspend fun getSaleNftDetail(marketId: Long): Flow<ApiResponse<FrameDetail>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.getSaleNftDetail(marketId) },
                        default = FrameDetail(),
                    ),
                )
            }

        override suspend fun likeNft(marketId: Long): Flow<ApiResponse<Long>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.likeNft(marketId) }, default = LikeResponse())
                when (response) {
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.likedCount))
                    is ApiResponse.Error -> emit(response)
                }
            }

        override suspend fun disLikeNft(marketId: Long): Flow<ApiResponse<Long>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.disLikeNft(marketId) },
                        default = LikeResponse(),
                    )
                when (response) {
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.likedCount))
                    is ApiResponse.Error -> emit(response)
                }
            }

        override suspend fun openNft(nftId: Long): Flow<ApiResponse<Unit>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.open(nftId) }, default = Unit))
            }

        override suspend fun getNftDetail(nftId: Long): Flow<ApiResponse<MyFrame>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.getNftDetail(nftId) }, default = MyFrame()))
            }

        override suspend fun getSaleLikeNft(): Flow<ApiResponse<List<FrameDetail>>> =
            flow {
                val response = emitApiResponse(apiResponse = { api.getSaleLikeNft() }, SearchFrame())
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data.content))
                }
            }

        override suspend fun purchaseNft(request: PurchaseNftRequest): Flow<ApiResponse<MyFrame>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.purchaseNft(request) }, default = MyFrame()))
            }

        override suspend fun cancelSaleNft(
            txHash: String,
            marketId: Long,
        ): Flow<ApiResponse<Unit>> =
            flow {
                emit(
                    emitApiResponse(apiResponse = {
                        api.cancelSaleNft(
                            txHash = txHash,
                            marketId = marketId,
                        )
                    }, default = Unit),
                )
            }

        override suspend fun getCreatorSaleNft(creator: Long): Flow<ApiResponse<List<FrameDetail>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getCreatorSaleNft(creator) },
                        default = SearchFrame(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data.content))
                }
            }

        override suspend fun getHotNft(): Flow<ApiResponse<List<FrameDetail>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getHotNft() },
                        default = listOf(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }
    }
