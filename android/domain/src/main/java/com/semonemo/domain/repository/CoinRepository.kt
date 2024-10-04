package com.semonemo.domain.repository

import com.semonemo.domain.model.ApiResponse
import com.semonemo.domain.model.Coin
import kotlinx.coroutines.flow.Flow

interface CoinRepository {
    suspend fun getBalance(): Flow<ApiResponse<Coin>>
}
