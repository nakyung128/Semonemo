package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.Auction
import com.semonemo.domain.model.AuctionJoinResponse
import com.semonemo.domain.model.AuctionRegisterRequest
import com.semonemo.domain.model.AuctionRegisterResponse
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface AuctionApi {
    @POST("api/auction")
    suspend fun registerAuction(
        @Body auction: AuctionRegisterRequest,
    ): BaseResponse<AuctionRegisterResponse>

    @GET("api/auction/{auctionId}")
    suspend fun getAuction(
        @Path("auctionId") auctionId: Long,
    ): BaseResponse<Auction>

    @GET("api/auction/all")
    suspend fun getAllAuction(): BaseResponse<List<Auction>>

    @GET("api/auction/{auctionId}/start")
    suspend fun startAuction(
        @Path("auctionId") auctionId: Long,
    ): BaseResponse<Nothing?>

    @GET("api/auction/{auctionId}/join")
    suspend fun joinAuction(
        @Path("auctionId") auctionId: Long,
    ): BaseResponse<AuctionJoinResponse>

    @GET("api/auction/{auctionId}/leave")
    suspend fun leaveAuction(
        @Path("auctionId") auctionId: Long,
    ): BaseResponse<Nothing?>
}
