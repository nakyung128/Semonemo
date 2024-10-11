package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.data.network.response.ExchangePayableResponse
import com.semonemo.data.network.response.GetBalanceResponse
import com.semonemo.data.network.response.GetCoinHistoryResponse
import com.semonemo.domain.model.CoinRate
import com.semonemo.domain.model.WeeklyCoin
import com.semonemo.domain.request.BuyCoinRequest
import com.semonemo.domain.request.ExchangePayableRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface CoinApi {
    @GET("api/coin")
    suspend fun getBalance(): BaseResponse<GetBalanceResponse>

    @GET("api/coin/history")
    suspend fun getCoinHistory(): BaseResponse<GetCoinHistoryResponse>

    @POST("api/coin/exchange/payable")
    suspend fun exchangeCoinPayable(
        @Body request: ExchangePayableRequest,
    ): BaseResponse<ExchangePayableResponse>

    @POST("api/coin/exchange/coin")
    suspend fun exchangePayableCoin(
        @Body request: ExchangePayableRequest,
    ): BaseResponse<ExchangePayableResponse>

    @GET("api/coin/price")
    suspend fun getCoinRates(): BaseResponse<CoinRate>

    @GET("api/coin/weekly")
    suspend fun getWeeklyCoin(): BaseResponse<List<WeeklyCoin>>

    @POST("api/coin/buy")
    suspend fun buyCoin(
        @Body request: BuyCoinRequest,
    ): BaseResponse<GetBalanceResponse>
}
