package com.semonemo.data.repository

import android.util.Log
import com.semonemo.data.network.api.AuctionApi
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionBidLog
import com.semonemo.domain.model.AuctionJoin
import com.semonemo.domain.model.AuctionJoinResponse
import com.semonemo.domain.model.AuctionRegisterRequest
import com.semonemo.domain.model.AuctionRegisterResponse
import com.semonemo.domain.repository.AuctionRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import javax.inject.Inject

class AuctionRepositoryImpl
    @Inject
    constructor(
        private val api: AuctionApi,
    ) : AuctionRepository {
        override suspend fun registerAuction(auction: AuctionRegisterRequest): Flow<ApiResponse<AuctionRegisterResponse>> =
            flow {
                emit(
                    emitApiResponse(
                        apiResponse = { api.registerAuction(auction) },
                        default = AuctionRegisterResponse(),
                    ),
                )
            }

        override suspend fun getAuction(auctionId: Long): Flow<ApiResponse<Auction>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAuction(auctionId) },
                        default = Auction(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }

        override suspend fun getAllAuction(): Flow<ApiResponse<List<Auction>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getAllAuction() },
                        default = listOf(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }

        override suspend fun startAuction(auctionId: Long): Flow<ApiResponse<Nothing?>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.startAuction(auctionId) },
                        default = null,
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }

        override suspend fun joinAuction(auctionId: Long): Flow<ApiResponse<AuctionJoin>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.joinAuction(auctionId) },
                        default = AuctionJoinResponse(),
                    )
                Log.d("Auction", "response : $response")
                val dateTimFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data =
                                    AuctionJoin(
                                        response.data.anonym,
                                        response.data.participants,
                                        bidLogs =
                                            response.data.bidLogs.map {
                                                AuctionBidLog(
                                                    userId = it.userId,
                                                    anonym = it.anonym,
                                                    bidAmount = it.bidAmount,
                                                    bidTime = LocalDateTime.parse(it.bidTime, dateTimFormat),
                                                    endTime = LocalDateTime.parse(it.endTime, dateTimFormat),
                                                )
                                            },
                                    ),
                            ),
                        )
                }
            }

        override suspend fun leaveAuction(auctionId: Long): Flow<ApiResponse<Nothing?>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.leaveAuction(auctionId) },
                        default = null,
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success -> emit(ApiResponse.Success(data = response.data))
                }
            }
    }
