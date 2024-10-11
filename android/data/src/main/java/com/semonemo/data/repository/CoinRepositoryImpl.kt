package com.semonemo.data.repository

import com.semonemo.data.network.api.CoinApi
import com.semonemo.data.network.response.ExchangePayableResponse
import com.semonemo.data.network.response.GetBalanceResponse
import com.semonemo.data.network.response.GetCoinHistoryResponse
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Coin
import com.semonemo.domain.model.CoinHistory
import com.semonemo.domain.model.CoinRate
import com.semonemo.domain.model.WeeklyCoin
import com.semonemo.domain.repository.CoinRepository
import com.semonemo.domain.request.BuyCoinRequest
import com.semonemo.domain.request.ExchangePayableRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import javax.inject.Inject

class CoinRepositoryImpl
    @Inject
    constructor(
        private val api: CoinApi,
    ) : CoinRepository {
        override suspend fun getBalance(): Flow<ApiResponse<Coin>> =
            flow {
                val response =
                    emitApiResponse(apiResponse = { api.getBalance() }, default = GetBalanceResponse())
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data =
                                    Coin(
                                        coinBalance = response.data.coinBalance,
                                        payableBalance = response.data.payableBalance,
                                    ),
                            ),
                        )
                }
            }

        override suspend fun getCoinHistory(): Flow<ApiResponse<List<CoinHistory>>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.getCoinHistory() },
                        default = GetCoinHistoryResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data = response.data.content,
                            ),
                        )
                }
            }

        override suspend fun exchangeCoinPayable(request: ExchangePayableRequest): Flow<ApiResponse<Coin>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.exchangeCoinPayable(request) },
                        default = ExchangePayableResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data =
                                    Coin(
                                        coinBalance = response.data.coinBalance,
                                        payableBalance = response.data.payableBalance,
                                    ),
                            ),
                        )
                }
            }

        override suspend fun exchangePayableCoin(request: ExchangePayableRequest): Flow<ApiResponse<Coin>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.exchangePayableCoin(request) },
                        default = ExchangePayableResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data =
                                    Coin(
                                        coinBalance = response.data.coinBalance,
                                        payableBalance = response.data.payableBalance,
                                    ),
                            ),
                        )
                }
            }

        override suspend fun getCoinRate(): Flow<ApiResponse<CoinRate>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.getCoinRates() }, default = CoinRate()))
            }

        override suspend fun getWeeklyCoin(): Flow<ApiResponse<List<WeeklyCoin>>> =
            flow {
                emit(emitApiResponse(apiResponse = { api.getWeeklyCoin() }, default = listOf()))
            }

        override suspend fun buyCoin(amount: Long): Flow<ApiResponse<Coin>> =
            flow {
                val response =
                    emitApiResponse(
                        apiResponse = { api.buyCoin(request = BuyCoinRequest(amount)) },
                        default = GetBalanceResponse(),
                    )
                when (response) {
                    is ApiResponse.Error -> emit(response)
                    is ApiResponse.Success ->
                        emit(
                            ApiResponse.Success(
                                data =
                                    Coin(
                                        coinBalance = response.data.coinBalance,
                                        payableBalance = response.data.payableBalance,
                                    ),
                            ),
                        )
                }
            }
    }
