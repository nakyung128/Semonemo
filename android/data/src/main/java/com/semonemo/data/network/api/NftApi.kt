package com.semonemo.data.network.api

import com.semonemo.data.network.response.BaseResponse
import com.semonemo.domain.model.FrameDetail
import com.semonemo.domain.model.Nft
import com.semonemo.domain.model.myFrame.GetMyFrameResponse
import com.semonemo.domain.request.PublishNftRequest
import com.semonemo.domain.request.SellNftRequest
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface NftApi {
    @POST("api/nft")
    suspend fun publishNft(
        @Body request: PublishNftRequest,
    ): BaseResponse<Nft>

    @GET("api/nft/users/{userId}/owned")
    suspend fun getUserNft(
        @Path("userId") userId: Long,
    ): BaseResponse<GetMyFrameResponse>

    @GET("api/nft/available")
    suspend fun getAvailableNft(
        @Query("type") type: Int,
    ): BaseResponse<GetMyFrameResponse>

    @POST("api/nft/sell")
    suspend fun sellRegisterNft(
        @Body request: SellNftRequest,
    ): BaseResponse<FrameDetail>
}
