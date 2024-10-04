package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.data.network.response.GetBalanceResponse
import retrofit2.http.GET

interface CoinApi {
    @GET("api/coin")
    suspend fun getBalance(): BaseResponse<GetBalanceResponse>
}
