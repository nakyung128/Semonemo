package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Coin
import com.semonemo.domain.model.CoinHistory
import com.semonemo.domain.model.CoinRate
import com.semonemo.domain.model.WeeklyCoin
import com.semonemo.domain.request.ExchangePayableRequest
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getBalance(): Flow<ApiResponse<Coin>>

    suspend fun getCoinHistory(): Flow<ApiResponse<List<CoinHistory>>>

    suspend fun exchangeCoinPayable(request: ExchangePayableRequest): Flow<ApiResponse<Coin>>

    suspend fun exchangePayableCoin(request: ExchangePayableRequest): Flow<ApiResponse<Coin>>

    suspend fun getCoinRate(): Flow<ApiResponse<CoinRate>>

    suspend fun getWeeklyCoin(): Flow<ApiResponse<List<WeeklyCoin>>>

    suspend fun buyCoin(amount: Long): Flow<ApiResponse<Coin>>
}
