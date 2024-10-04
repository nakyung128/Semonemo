package com.semonemo.data.repository

import android.util.Log
import com.semonemo.data.network.api.NftApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.SearchFrame
import com.semonemo.domain.model.myFrame.GetMyFrameResponse
import com.semonemo.domain.model.myFrame.MyFrame
import com.semonemo.domain.repository.NftRepository
import com.semonemo.domain.request.PublishNftRequest
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
                Log.d("jaehan", "publish nft : $response")
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

        override suspend fun getAllSaleNft(): Flow<ApiResponse<List<FrameDetail>>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.getAllSaleNft() }, default = SearchFrame())
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(response.data.content))
                }
            }
    }
