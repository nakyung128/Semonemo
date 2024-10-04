package com.semonemo.data.repository

import com.semonemo.data.network.api.CoinApi
import com.semonemo.data.network.response.GetBalanceResponse
import com.semonemo.data.network.response.emitApiResponse
import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Coin
import com.semonemo.domain.repository.CoinRepository
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
    }
